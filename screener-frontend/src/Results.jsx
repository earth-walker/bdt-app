import { createSignal, For } from "solid-js";

const results = { Dish: "Roastbeef" };

export default function Results() {
  const [dmnResult] = createSignal([
    { name: "Item A", value: 100, category: "Gold" },
    { name: "Item B", value: 200, category: "Silver" },
  ]);

  return (
    <div class="my-4 mx-8 p-4 border border-gray-300 rounded shadow-sm">
      <h2 class="text-gray-800 text-md font-bold">Results</h2>
      <div class="my-2">
        <For each={dmnResult()}>
          {(item) => (
            <div class="border-t border-gray-300 p-2 ">
              <div class="flex flex-col">
                <For each={Object.entries(item)}>
                  {([key, value]) => (
                    <div class="flex text-sm text-gray-700">
                      <span class="font-medium capitalize">{key}:</span>
                      <span>{value?.toString()}</span>
                    </div>
                  )}
                </For>
              </div>
            </div>
          )}
        </For>
      </div>
    </div>
  );
}
