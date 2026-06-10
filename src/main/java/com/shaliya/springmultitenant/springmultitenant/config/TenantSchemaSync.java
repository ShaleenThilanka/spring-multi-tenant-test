package com.shaliya.springmultitenant.springmultitenant.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantSchemaSync {

    @Autowired
    private Environment env;

    private static final String ENTITY_PACKAGE =
            "com.shaliya.springmultitenant.springmultitenant.entity";

    // Tables that live ONLY in master_db — never create these in tenant DBs
    private static final Set<String> MASTER_ONLY_TABLES = Set.of(
            "user", "business", "user_role", "user_has_user_role"
    );

    // ─── Main Entry Point ─────────────────────────────────────────────────────

    public void syncTenantSchema(String tenantId) {
        if (tenantId == null || tenantId.equals("master_db")) return;

        System.out.println("[TenantSchemaSync] Starting schema sync for tenant: " + tenantId);

        List<Class<?>> tenantEntities = scanTenantEntities();
        if (tenantEntities.isEmpty()) {
            System.out.println("[TenantSchemaSync] No tenant entities found to sync.");
            return;
        }

        HikariDataSource tenantDs = buildDataSource(
                String.format("jdbc:mysql://localhost:3306/%s", tenantId)
        );

        try {
            JdbcTemplate jdbc = new JdbcTemplate(tenantDs);
            for (Class<?> entityClass : tenantEntities) {
                syncEntity(jdbc, entityClass, tenantId);
            }
            System.out.println("[TenantSchemaSync] Schema sync complete for tenant: " + tenantId);
        } catch (Exception e) {
            System.err.println("[TenantSchemaSync] Error syncing tenant "
                    + tenantId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            tenantDs.close();
        }
    }

    // ─── Entity Scanner ───────────────────────────────────────────────────────

    private List<Class<?>> scanTenantEntities() {
        List<Class<?>> result = new ArrayList<>();
        try {
            ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

            for (BeanDefinition bd : scanner.findCandidateComponents(ENTITY_PACKAGE)) {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                String tableName = resolveTableName(clazz);

                if (MASTER_ONLY_TABLES.contains(tableName.toLowerCase())) {
                    System.out.println("[TenantSchemaSync] Skipping master-only: " + tableName);
                    continue;
                }

                result.add(clazz);
                System.out.println("[TenantSchemaSync] Queued for sync: "
                        + clazz.getSimpleName() + " → " + tableName);
            }
        } catch (Exception e) {
            System.err.println("[TenantSchemaSync] Entity scan failed: " + e.getMessage());
        }
        return result;
    }

    // ─── Table + Column Sync ──────────────────────────────────────────────────

    private void syncEntity(JdbcTemplate jdbc, Class<?> entityClass, String tenantId) {
        String tableName = resolveTableName(entityClass);
        boolean tableExists = checkTableExists(jdbc, tenantId, tableName);

        if (!tableExists) {
            String createSql = generateCreateTableSql(entityClass, tableName);
            jdbc.execute(createSql);
            System.out.println("[TenantSchemaSync] Created table: " + tableName);
        } else {
            syncColumns(jdbc, entityClass, tableName, tenantId);
        }
    }

    private boolean checkTableExists(JdbcTemplate jdbc, String tenantId, String tableName) {
        String sql = """
                SELECT COUNT(*) FROM information_schema.tables
                WHERE table_schema = ? AND table_name = ?
                """;
        Integer count = jdbc.queryForObject(sql, Integer.class, tenantId, tableName);
        return count != null && count > 0;
    }

    private void syncColumns(JdbcTemplate jdbc, Class<?> entityClass,
                             String tableName, String tenantId) {
        String sql = """
                SELECT column_name FROM information_schema.columns
                WHERE table_schema = ? AND table_name = ?
                """;
        List<String> existingColumns = jdbc.queryForList(sql, String.class, tenantId, tableName)
                .stream()
                .map(String::toLowerCase)
                .toList();

        for (Field field : getAllFields(entityClass)) {
            if (shouldSkipField(field)) continue;

            String columnName = resolveColumnName(field);
            if (!existingColumns.contains(columnName.toLowerCase())) {
                String alterSql = generateAddColumnSql(tableName, columnName, field);
                jdbc.execute(alterSql);
                System.out.println("[TenantSchemaSync] Added column: "
                        + tableName + "." + columnName);
            }
        }
    }

    // ─── SQL Generators ───────────────────────────────────────────────────────

    private String generateCreateTableSql(Class<?> entityClass, String tableName) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS `")
                .append(tableName).append("` (\n");

        List<String> columnDefs = new ArrayList<>();
        String primaryKeyColumn = null;

        for (Field field : getAllFields(entityClass)) {
            if (shouldSkipField(field)) continue;

            String columnName = resolveColumnName(field);
            String columnType = resolveColumnType(field);

            StringBuilder colDef = new StringBuilder("`")
                    .append(columnName).append("` ").append(columnType);

            Column colAnnotation = field.getAnnotation(Column.class);
            if (colAnnotation != null && !colAnnotation.nullable()) {
                colDef.append(" NOT NULL");
            }

            // Unique constraint
            if (colAnnotation != null && colAnnotation.unique()) {
                colDef.append(" UNIQUE");
            }

            columnDefs.add(colDef.toString());

            if (field.isAnnotationPresent(Id.class)) {
                primaryKeyColumn = columnName;
            }
        }

        sql.append(String.join(",\n", columnDefs));

        if (primaryKeyColumn != null) {
            sql.append(",\nPRIMARY KEY (`").append(primaryKeyColumn).append("`)");
        }

        sql.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
        return sql.toString();
    }

    private String generateAddColumnSql(String tableName, String columnName, Field field) {
        String columnType = resolveColumnType(field);
        StringBuilder sb = new StringBuilder("ALTER TABLE `")
                .append(tableName).append("` ADD COLUMN `")
                .append(columnName).append("` ").append(columnType);

        Column colAnnotation = field.getAnnotation(Column.class);
        if (colAnnotation != null && !colAnnotation.nullable()) {
            // Use DEFAULT to allow NOT NULL on existing rows
            sb.append(" NOT NULL DEFAULT ''");
        } else {
            sb.append(" DEFAULT NULL");
        }

        return sb.toString();
    }

    // ─── Reflection Helpers ───────────────────────────────────────────────────

    private String resolveTableName(Class<?> entityClass) {
        Table tableAnnotation = AnnotationUtils.findAnnotation(entityClass, Table.class);
        if (tableAnnotation != null && !tableAnnotation.name().isEmpty()) {
            return tableAnnotation.name();
        }
        return toSnakeCase(entityClass.getSimpleName());
    }

    private String resolveColumnName(Field field) {
        Column colAnnotation = field.getAnnotation(Column.class);
        if (colAnnotation != null && !colAnnotation.name().isEmpty()) {
            return colAnnotation.name();
        }
        JoinColumn joinCol = field.getAnnotation(JoinColumn.class);
        if (joinCol != null && !joinCol.name().isEmpty()) {
            return joinCol.name();
        }
        return toSnakeCase(field.getName());
    }

    private String resolveColumnType(Field field) {
        Column colAnnotation = field.getAnnotation(Column.class);
        int length = (colAnnotation != null) ? colAnnotation.length() : 255;

        // Check for @Lob — use TEXT/LONGTEXT
        if (field.isAnnotationPresent(Lob.class)) {
            return "LONGTEXT";
        }

        // Check for @Temporal
        Temporal temporal = field.getAnnotation(Temporal.class);
        if (temporal != null) {
            return switch (temporal.value()) {
                case DATE -> "DATE";
                case TIME -> "TIME";
                case TIMESTAMP -> "DATETIME";
            };
        }

        // Check for @Enumerated
        if (field.isAnnotationPresent(Enumerated.class)) {
            return "VARCHAR(50)";
        }

        // Check for @JoinColumn — FK stored as VARCHAR matching the PK type
        if (field.isAnnotationPresent(JoinColumn.class)) {
            return "VARCHAR(80)";
        }

        Class<?> type = field.getType();

        if (type == String.class)            return "VARCHAR(" + length + ")";
        if (type == Long.class
                || type == long.class)       return "BIGINT";
        if (type == Integer.class
                || type == int.class)        return "INT";
        if (type == Short.class
                || type == short.class)      return "SMALLINT";
        if (type == Double.class
                || type == double.class)     return "DOUBLE";
        if (type == Float.class
                || type == float.class)      return "FLOAT";
        if (type == Boolean.class
                || type == boolean.class)    return "TINYINT(1)";
        if (type == BigDecimal.class)        return "DECIMAL(19,4)";
        if (type == LocalDate.class
                || type == java.util.Date.class) return "DATE";
        if (type == LocalDateTime.class)     return "DATETIME";
        if (type == byte[].class)            return "LONGBLOB";

        // Fallback for unknown types
        return "TEXT";
    }

    private boolean shouldSkipField(Field field) {
        if (field.isAnnotationPresent(Transient.class))   return true;
        if (field.isAnnotationPresent(OneToMany.class))   return true;
        if (field.isAnnotationPresent(ManyToMany.class))  return true;
        if (field.isAnnotationPresent(ManyToOne.class)
                && !field.isAnnotationPresent(JoinColumn.class)) return true;
        if (Modifier.isStatic(field.getModifiers()))      return true;
        return false;
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    private String toSnakeCase(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    // ─── DataSource Builder ───────────────────────────────────────────────────

    private HikariDataSource buildDataSource(String url) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setMaximumPoolSize(2);
        ds.setConnectionTimeout(30000);
        return ds;
    }
}