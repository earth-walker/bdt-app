import { onMount, onCleanup, createSignal } from "solid-js";
import { FormEditor } from "@bpmn-io/form-js-editor";
import { getSelectedProjectFromStorage } from "../../storageUtils/storageUtils";
import { saveFormSchema } from "../../api/screener";
import {
  saveFormSchemaToStorageDebounced,
  getFormSchemaFromStorage,
} from "../../storageUtils/storageUtils";
import "@bpmn-io/form-js/dist/assets/form-js.css";
import "@bpmn-io/form-js-editor/dist/assets/form-js-editor.css";

function FormEditorView() {
  const [isUnsaved, setIsUnsaved] = createSignal(false);
  const [isSaving, setIsSaving] = createSignal(false);
  let timeoutId;
  let container;
  let formEditor;
  let emptySchema = {
    components: [],
    exporter: { name: "form-js (https://demo.bpmn.io)", version: "1.15.0" },
    id: "Form_1sgem74",
    schemaVersion: 18,
    type: "default",
  };

  onMount(() => {
    formEditor = new FormEditor({ container });

    let formSchema = getFormSchemaFromStorage();

    if (formSchema) {
      formEditor.importSchema(formSchema).catch((err) => {
        console.error("Failed to load schema", err);
      });
    } else {
      formEditor.importSchema(emptySchema).catch((err) => {
        console.error("Failed to load schema", err);
      });
    }

    formEditor.on("changed", (e) => {
      setIsUnsaved(true);
      saveFormSchemaToStorageDebounced(e.schema);
    });

    onCleanup(() => {
      if (formEditor) {
        formEditor.destroy();
        formEditor = null;
        clearTimeout(timeoutId);
      }
    });
  });

  const handleSave = async () => {
    const selectedProject = getSelectedProjectFromStorage();
    const screenerId = selectedProject.id;
    const formSchema = await formEditor.getSchema();
    setIsUnsaved(false);
    setIsSaving(true);
    saveFormSchema(screenerId, formSchema);
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => setIsSaving(false), 500);
  };

  return (
    <>
      <div className="overflow-auto">
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

export default FormEditorView;
