import "./App.css";
import { createSignal, onMount } from "solid-js";
import { createNewScreener, fetchProject } from "./api/api";
import {
  getSelectedProjectFromStorage,
  clearSessionStorage,
  saveScreenerDataToStorage,
} from "./storageUtils/storageUtils";
import Project from "./components/projectEditor/Project";
import ProjectsList from "./components/projectsList/projectsList";
import Loading from "./components/Loading";
import AuthForm from "./components/auth/AuthForm";
import { useAuth } from "./context/AuthContext";

function Routes() {
  const [selectedProject, setSelectedProject] = createSignal();
  const [isLoading, setIsLoading] = createSignal(false);
  const { user } = useAuth();
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

  function content() {
    if (!user()) {
      return <AuthForm></AuthForm>;
    } else if (isLoading()) {
      return <Loading></Loading>;
    } else if (!selectedProject()) {
      return (
        <ProjectsList
          handleCreateNewScreener={handleCreateNewScreener}
          setSelectedProject={handleSelectProject}
        ></ProjectsList>
      );
    } else {
      return (
        <Project
          selectedProject={selectedProject}
          setSelectedProject={handleSelectProject}
        ></Project>
      );
    }
  }

  return <>{content()}</>;
}

export default Routes;
