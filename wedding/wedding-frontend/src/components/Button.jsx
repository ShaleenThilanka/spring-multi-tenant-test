const variants = {
  primary: 'bg-moss text-ivory hover:bg-fern',
  outline: 'border border-moss/40 text-moss hover:border-fern hover:text-fern',
  ghost: 'bg-canopy text-ivory hover:bg-moss',
};

export default function Button({ variant = 'primary', className = '', children, ...props }) {
  return (
    <button
      className={`inline-flex h-12 items-center justify-center rounded-full px-6 py-3 font-body text-sm uppercase tracking-[0.15em] transition-colors disabled:cursor-not-allowed disabled:opacity-50 ${variants[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
