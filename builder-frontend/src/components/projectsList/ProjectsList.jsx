import { For, createResource, createSignal } from "solid-js";
import Header from "../Header";
import { fetchProjects } from "../../api/api";
import NewScreenerForm from "./NewScreenerForm";
export default function ProjectsList({
  setSelectedProject,
  handleCreateNewScreener,
  clearUserState,
}) {
  const [data] = createResource(fetchProjects);
  const [isModalVisible, setIsModalVisible] = createSignal(false);

  return (
    <>
      <div>
        <Header
          returnToProjectsList={() => setSelectedProject(undefined)}
          clearUserState={clearUserState}
        ></Header>
        <Show when={data} fallback={<div>Loading...</div>}>
          <div className="flex flex-wrap gap-4 p-4 w-100">
            <div
              onClick={() => setIsModalVisible(true)}
              class="rounded-lg p-4 w-80 h-60 flex justify-center border-6 shadow-md border-gray-300  hover:shadow-lg hover:bg-gray-100"
            >
              <div className="flex items-center text-2xl font-bold">
                Create new screener
              </div>
            </div>
            <For each={data()}>
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
        </Show>
      </div>
      {isModalVisible() && (
        <NewScreenerForm
          handleCreateNewScreener={handleCreateNewScreener}
          setIsModalVisible={setIsModalVisible}
        ></NewScreenerForm>
      )}
    </>
  );
}
