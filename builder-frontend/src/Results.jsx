import { For } from "solid-js";

export default function Results({ results }) {
  return (
    <div class="my-4 mx-8 p-4 border border-gray-300 rounded shadow-sm">
      <div class="text-lg text-gray-800 text-md font-bold">Results</div>
      <div class="my-2">
        {results() && (
          <For each={results()}>
            {(item) =>
              item && (
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
              )
            }
          </For>
        )}
      </div>
    </div>
  );
}
