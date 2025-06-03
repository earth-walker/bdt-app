import "./App.css";
import { createSignal, onMount } from "solid-js";
import Project from "./Project";
import ProjectsList from "./ProjectsList";

function App() {
  const [selectedProject, setSelectedProject] = createSignal();
  const projectName = "Test Project";

  onMount(() => {
    const project = sessionStorage.getItem("selectedProject");
    console.log(project);
    if (project) {
      setSelectedProject(JSON.parse(project));
    }
  });

  const handleSelectProject = (project) => {
    if (!project) {
      setSelectedProject(undefined);
      sessionStorage.removeItem("selectedProject");
    }
    setSelectedProject(project);
    sessionStorage.setItem("selectedProject", JSON.stringify(project));
  };

  return (
    <>
      {selectedProject() && (
        <Project setSelectedProject={handleSelectProject}></Project>
      )}
      {!selectedProject() && (
        <ProjectsList setSelectedProject={handleSelectProject}></ProjectsList>
      )}
    </>
  );
}

export default App;
