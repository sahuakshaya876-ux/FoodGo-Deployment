export default function LoadingSpinner({ label = "Loading..." }) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 py-16 text-slate-400">
      <div className="h-10 w-10 animate-spin rounded-full border-4 border-slate-200 border-t-brand-500" />
      <p className="text-sm">{label}</p>
    </div>
  );
}
