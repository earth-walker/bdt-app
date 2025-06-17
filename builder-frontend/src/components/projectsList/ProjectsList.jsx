import { For, createResource, createSignal } from "solid-js";
import Header from "../Header";
import { fetchProjects } from "../../api/api";
import NewScreenerForm from "./NewScreenerForm";
import MenuIcon from "../icon/MenuIcon";
import EditScreenerForm from "./EditScreenerForm";
export default function ProjectsList({
  setSelectedProject,
  handleCreateNewScreener,
  handleEditScreener,
  clearUserState,
}) {
  const [data, { refetch }] = createResource(fetchProjects);
  const [isNewScreenerModalVisible, setIsNewScreenerModalVisible] =
    createSignal(false);
  const [isEditModalVisible, setIsEditgModalVisible] = createSignal(false);
  const [editModelData, setEditModalData] = createSignal();

  const handleProjectMenuClicked = (e, screenerData) => {
    e.stopPropagation();
    setEditModalData(screenerData);
    setIsEditgModalVisible(true);
  };

  const handleUpdateScreenerClicked = async (updateData) => {
    try {
      await handleEditScreener(updateData);
      refetch();
      setIsEditgModalVisible(false);
    } catch (e) {
      return e;
    }
  };

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
                    <div className="flex justify-between">
                      <div className="text-xl font-bold">
                        {item.screenerName}
                      </div>
                      <div
                        className="hover:cursor-pointer"
                        onClick={(e) => handleProjectMenuClicked(e, item)}
                      >
                        <MenuIcon></MenuIcon>
                      </div>
                    </div>
                  </div>
                )
              }
            </For>
          </div>
        </Show>
      </div>
      {isNewScreenerModalVisible() && (
        <NewScreenerForm
          handleCreateNewScreener={handleCreateNewScreener}
          setIsModalVisible={setIsNewScreenerModalVisible}
        ></NewScreenerForm>
      )}
      {isEditModalVisible() && (
        <EditScreenerForm
          handleEditScreener={handleUpdateScreenerClicked}
          setIsEditModalVisible={setIsEditgModalVisible}
          screenerData={editModelData()}
        ></EditScreenerForm>
      )}
    </>
  );
}
