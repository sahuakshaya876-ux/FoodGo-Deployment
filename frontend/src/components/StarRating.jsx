import { Star } from "lucide-react";

export default function StarRating({ rating = 0, size = 14 }) {
  const rounded = Math.round(rating);
  return (
    <div className="flex items-center gap-0.5">
      {[1, 2, 3, 4, 5].map((n) => (
        <Star
          key={n}
          size={size}
          className={n <= rounded ? "fill-amber-400 text-amber-400" : "fill-slate-200 text-slate-200"}
        />
      ))}
    </div>
  );
}
