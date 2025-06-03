import { For } from "solid-js";
import Header from "./Header";
export default function ProjectsList({ setSelectedProject }) {
  const data = [
    { screenerName: "Test Screener" },
    { screenerName: "Philly Tax Benefit" },
  ];

  return (
    <div>
      <Header
        returnToProjectsList={() => setSelectedProject(undefined)}
      ></Header>
      <div className="flex gap-4 p-4 w-100">
        <For each={data}>
          {(item) =>
            item && (
              <div
                onClick={() => setSelectedProject(item)}
                class="rounded-lg p-4 w-80 h-60 border-1 shadow-md border-gray-300  hover:shadow-lg hover:bg-gray-100"
              >
                <div className="text-xl font-bold">{item.screenerName}</div>
              </div>
            )
          }
        </For>
      </div>
    </div>
  );
}
