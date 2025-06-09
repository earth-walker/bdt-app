import "./App.css";
import { createSignal, onMount } from "solid-js";
import { createNewScreener, fetchProject } from "./api/api";
import Project from "./Project";
import ProjectsList from "./ProjectsList";
import Loading from "./Loading";

function App() {
  const [selectedProject, setSelectedProject] = createSignal();
  const [isLoading, setIsLoading] = createSignal(false);
  const projectName = "Test Project";

  onMount(() => {
    const project = sessionStorage.getItem("selectedProject");
    if (project && project != "undefined") {
      setSelectedProject(JSON.parse(project));
    }
  });

  const handleSelectProject = async (screener) => {
    try {
      if (!screener) {
        setSelectedProject(undefined);
        sessionStorage.removeItem("selectedProject");
      }
      setIsLoading(true);
      const screenerData = await fetchProject(screener.id);
      setSelectedProject(screenerData);
      sessionStorage.setItem("selectedProject", JSON.stringify(screenerData));
      console.log("Screener Data fetched: ");
      console.log(screenerData);
      setIsLoading(false);
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
      {selectedProject() && (
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
      {isLoading() && <Loading></Loading>}
    </>
  );
}

export default App;
