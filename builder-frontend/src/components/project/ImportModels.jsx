import { createResource, createSignal } from "solid-js";
import { fetchAvailableModels } from "../../api/models";
import ModelDetail from "./ModelDetail";
const models = () => {
  return [
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
    {
      name: "List Types",
      groupId: "Benefits-Decision-Toolkit",
      artifactId: "List-Types",
      version: "1.0.0",
      description: "A set of list utility types.",
    },
  ];
};

const fetchImportedModels = async () => {
  return Promise.resolve(models());
};

export default function ImportModels() {
  const [importedModels, { refetchImportedModels }] =
    createResource(fetchImportedModels);
  const [availableModels, { refetchAvailableModels }] =
    createResource(fetchAvailableModels);
  const [showImportedModels, setShowImportedModels] = createSignal(false);
  const [showAvailableModels, setShowAvailableModels] = createSignal(false);
  const [selectedModel, setSelectedModel] = createSignal();

  return (
    <div className="flex flex-1 overflow-hidden">
      {/* Left Pane: Scrollable list */}
      <div className="w-1/3 h-full overflow-y-auto border-r border-gray-300">
        {/* Imported Models Header */}
        <div
          onClick={() => setShowImportedModels(!showImportedModels())}
          className="p-4 border-b flex justify-between items-center bg-gray-100 hover:cursor-pointer"
        >
          <span className="font-semibold text-gray-700">Imported Models</span>
          <button>{showImportedModels() ? "−" : "+"}</button>
        </div>

        {/* Imported Models List */}
        {showImportedModels() && importedModels() && (
          <div className="border-b">
            {models().map((model, index) => (
              <div
                onClick={() => setSelectedModel(model)}
                key={index}
                className="p-4 border-b border-gray-200 cursor-pointer hover:bg-gray-100"
              >
                <div className="text-gray-900 font-bold">{model.name}</div>
                <div className="text-gray-700 text-sm">{model.description}</div>
                <div className="text-gray-500 text-xs">{model.groupId}</div>
              </div>
            ))}
          </div>
        )}
        {/* Available Models Header */}
        <div
          onClick={() => setShowAvailableModels(!showAvailableModels())}
          className="p-4 border-b flex justify-between items-center bg-gray-100 hover:cursor-pointer"
        >
          <span className="font-semibold text-gray-700">Available Models</span>
          <button>{showAvailableModels() ? "−" : "+"}</button>
        </div>
        {showAvailableModels() &&
          availableModels() &&
          availableModels().map((model, index) => (
            <div
              onClick={() => setSelectedModel(model)}
              key={index}
              className="p-4 border-b border-gray-200 cursor-pointer hover:bg-gray-100"
            >
              <div className="text-gray-900 font-bold">{model.name}</div>
              <div className="text-gray-700 text-sm">
                {model.shortDescription}
              </div>
              <div className="text-gray-500 text-xs">{model.groupId}</div>
            </div>
          ))}
      </div>
      {/* Model Details window */}
      <div className="flex-1 h-full p-6 overflow-y-auto">
        {!!selectedModel() ? (
          <ModelDetail
            model={selectedModel()}
            isImported={false}
            onImport={() => console.log("on import")}
            onRemove={() => console.log("On remove")}
            deselect={() => setSelectedModel()}
          ></ModelDetail>
        ) : (
          <>
            <div className="text-2xl text-gray-800 font-bold">
              Include external models in your screener project.
            </div>
            <div className="pt-4 text-gray-700">
              Import pre-built models to use in your project. When you import an
              external DMN model, it's elements are avilable to use in the DMN
              editor. DMN decisions and other elements from an imported model
              cannot be modified.
            </div>
          </>
        )}
      </div>
    </div>
  );
}
