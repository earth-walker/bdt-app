import "./App.css";
import { createSignal, onMount, createEffect } from "solid-js";
import { createNewScreener, fetchProject } from "./api/screener";
import {
  getSelectedProjectFromStorage,
  clearSessionStorage,
  saveScreenerDataToStorage,
} from "./storageUtils/storageUtils";
import Project from "./components/project/Project";
import ProjectsList from "./components/projectsList/ProjectsList";
import Loading from "./components/Loading";
import AuthForm from "./components/auth/AuthForm";
import { useAuth } from "./context/AuthContext";
import KogitoDmnEditorView from "./components/project/KogitoDmnEditorView";

function App() {
  const [selectedProject, setSelectedProject] = createSignal();
  const [isLoading, setIsLoading] = createSignal(false);
  const { user } = useAuth();

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
        await fetchAndCacheProject(screener.id);
        setIsLoading(false);
      }
    } catch (e) {
      console.log("Error fetching screener data", e);
      setIsLoading(false);
    }
  };

  const fetchAndCacheProject = async (screenerId) => {
    const screenerData = await fetchProject(screenerId);
    saveScreenerDataToStorage(screenerData);
    setSelectedProject(screenerData);
  };

  const clearUserState = () => {
    setSelectedProject(null);
  };

  const handleCreateNewScreener = async (screenerData) => {
    try {
      const newScreener = await createNewScreener(screenerData);
      setSelectedProject(newScreener);
      sessionStorage.setItem("selectedProject", JSON.stringify(newScreener));
    } catch (e) {
      console.log("Error creating screener", e);
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
          clearUserState={clearUserState}
        ></ProjectsList>
      );
    } else {
      return (
        <Project
          selectedProject={selectedProject}
          setSelectedProject={handleSelectProject}
          clearUserState={clearUserState}
          fetchAndCacheProject={fetchAndCacheProject}
        ></Project>
      );
    }
  }

  return <>{content()}</>;
}

export default App;
