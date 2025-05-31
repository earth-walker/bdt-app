import { onMount, onCleanup } from "solid-js";
import DmnModeler from "dmn-js/lib/Modeler";
import "dmn-js/dist/assets/diagram-js.css";
import "dmn-js/dist/assets/dmn-js-boxed-expression-controls.css";
import "dmn-js/dist/assets/dmn-js-boxed-expression.css";
import "dmn-js/dist/assets/dmn-js-decision-table-controls.css";
import "dmn-js/dist/assets/dmn-js-decision-table.css";
import "dmn-js/dist/assets/dmn-js-drd.css";
import "dmn-js/dist/assets/dmn-js-literal-expression.css";
import "dmn-js/dist/assets/dmn-js-shared.css";
function DmnEditorView({ schema, submitForm }) {
  let container;
  let modeler;

  onMount(() => {
    const modeler = new DmnModeler({ container });

    const initialXML = `<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="https://www.omg.org/spec/DMN/20191111/MODEL/" id="definitions_0eoh294" name="definitions" namespace="http://camunda.org/schema/1.0/dmn" exporter="dmn-js (https://demo.bpmn.io/dmn)" exporterVersion="17.2.0">
  <decision id="decision_0codji8" name="">
    <decisionTable id="decisionTable_1jf6567">
      <input id="input1" label="">
        <inputExpression id="inputExpression1" typeRef="string">
          <text></text>
        </inputExpression>
      </input>
      <output id="output1" label="" name="" typeRef="string" />
    </decisionTable>
  </decision>
</definitions>`;

    // Load a sample or empty diagram

    modeler.importXML(initialXML).catch((err) => {
      console.error("Failed to import DMN XML", err);
    });

    onCleanup(() => {
      modeler.destroy();
    });
  });

  return (
    <div className="overflow-auto">
      <div className="h-full" ref={(el) => (container = el)} />
    </div>
  );
}

export default DmnEditorView;
