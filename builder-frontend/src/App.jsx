import "./App.css";
import { createSignal, onMount } from "solid-js";
import { createNewScreener, fetchProject } from "./api/api";
import {
  getSelectedProjectFromStorage,
  clearSessionStorage,
  saveScreenerDataToStorage,
} from "./storageUtils/storageUtils";
import Project from "./Project";
import ProjectsList from "./ProjectsList";
import Loading from "./Loading";
import { conforms } from "lodash";
import AuthForm from "./AuthForm";

function App() {
  const [selectedProject, setSelectedProject] = createSignal();
  const [isLoading, setIsLoading] = createSignal(false);
  const projectName = "Test Project";

  onMount(() => {
    const project = getSelectedProjectFromStorage();
    if (project) {
      setSelectedProject(project);
    }
  });

  const handleSelectProject = async (screener) => {
    try {
      if (!screener) {
        setSelectedProject(undefined);
        clearSessionStorage();
      } else {
        setIsLoading(true);
        const screenerData = await fetchProject(screener.id);
        saveScreenerDataToStorage(screenerData);
        setSelectedProject(screenerData);
        setIsLoading(false);
      }
    } catch (e) {
      console.log("Error fetching screener data", e);
      setIsLoading(false);
    }
  };

  const handleCreateNewScreener = async (screenerData) => {
    try {
      const newScreener = await createNewScreener(screenerData);
      setSelectedProject(newScreener);
      sessionStorage.setItem("selectedProject", JSON.stringify(project));
    } catch (e) {
      console.log("Error creating screener with data:");
      console.log(screenerData);
    }
  };

  return (
    <>
      <AuthForm></AuthForm>
      {/* {selectedProject() && (
        <Project
          selectedProject={selectedProject}
          setSelectedProject={handleSelectProject}
        ></Project>
      )}
      {!selectedProject() && (
        <ProjectsList
          handleCreateNewScreener={handleCreateNewScreener}
          setSelectedProject={handleSelectProject}
        ></ProjectsList>
      )}
      {isLoading() && <Loading></Loading>} */}
    </>
  );
}

export default App;
