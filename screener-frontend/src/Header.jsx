export default function Header({ orgName }) {
  return (
    <header class="bg-sky-600 px-8 py-8">
      <h1 class="text-4xl font-bold text-white">{orgName}</h1>
    </header>
  );
}
