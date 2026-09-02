export default function EmptyState({ icon: Icon, title, description, action }) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 rounded-2xl border border-dashed border-slate-200 bg-white px-6 py-16 text-center">
      {Icon && <Icon size={40} className="text-slate-300" />}
      <h3 className="text-lg font-semibold text-slate-700">{title}</h3>
      {description && <p className="max-w-sm text-sm text-slate-500">{description}</p>}
      {action}
    </div>
  );
}
