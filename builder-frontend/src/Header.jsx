export default function Header() {
  return (
    <header class="bg-gray-200  px-4 py-3 flex items-center justify-between">
      <div class="flex items-center space-x-6">
        <span class="text-lg font-bold text-gray-600">
          Benefits Decision Toolkit
        </span>
      </div>
      <span href="/projects" class="text-sm text-blue-500 hover:underline">
        ← Back to Projects
      </span>
    </header>
  );
}
