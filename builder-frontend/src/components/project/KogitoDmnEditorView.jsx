import * as DmnEditor from "@kogito-tooling/kie-editors-standalone/dist/dmn";
import { createSignal, onCleanup, onMount } from "solid-js";
import { debounce } from "lodash";
import {
  getDmnModelFromStorage,
  saveDmnModelToStorageDebounced,
  getSelectedProjectFromStorage,
} from "../../storageUtils/storageUtils";
import { saveDmnModel } from "../../api/api";

export default function KogitoDmnEditorView() {
  const [isUnsaved, setIsUnsaved] = createSignal("Not Dirty");
  const [isSaving, setIsSaving] = createSignal(false);
  let container;
  let editor;
  let timeoutId;

  onMount(() => {
    // Initialize the DMN Editor directly without the envelope

    let initialDmn = getDmnModelFromStorage();
    if (!initialDmn) initialDmn = "";
    else {
      const firstChar = initialDmn.charAt(0); // Get the first character
      const lastChar = initialDmn.charAt(initialDmn.length - 1); // Get the last character
      if (firstChar == '"' && lastChar == '"')
        initialDmn = initialDmn.slice(1, -1);
    }

    editor = DmnEditor.open({
      container: container, // Reference to the DOM container where the editor will be embedded
      initialContent: Promise.resolve(initialDmn), // Initial content of the editor (empty string in this case)
      readOnly: false, // Set to true if you want the editor to be read-only
    });

    editor.subscribeToContentChanges(async (isDirty) => {
      if (isDirty) {
        setIsUnsaved("Dirty");
      } else {
        setIsUnsaved("Not Dirty");
      }
    });
  });

  onCleanup(() => {
    // Cleanup the editor when the component is unmounted
    if (editor) editor.close();
  });

  const handleSave = async () => {
    const selectedProject = getSelectedProjectFromStorage();
    const screenerId = selectedProject.id;
    const xml = await editor.getContent();
    setIsUnsaved(false);
    setIsSaving(true);
    saveDmnModelToStorageDebounced(xml);
    saveDmnModel(screenerId, xml);
    setIsSaving(false);
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => setIsSaving(false), 500);
  };

  return (
    <>
      <div className="h-full overflow-auto">
        <div className="h-full" ref={(el) => (container = el)} />
      </div>
      <div className="fixed z-50 top-16 right-4 flex ml-auto mr-8 gap-2 justify-center">
        {isUnsaved() && (
          <span className="underline text-sm flex items-center text-gray-500">
            unsaved changes
          </span>
        )}
        {isSaving() && (
          <span className="text-sm flex items-center text-gray-500">
            saving ...
          </span>
        )}
        <button
          onClick={handleSave}
          className="px-2 text-emerald-500 h-8 border-2 rounded hover:bg-emerald-100"
        >
          Save
        </button>
      </div>
    </>
  );
}
