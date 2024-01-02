
function randomStr() {
  return Math.random().toString(36).slice(-8)
}

export default function() {
  return `<?xml version="1.0" encoding="UTF-8"?>
          <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:bioc="http://bpmn.io/schema/bpmn/biocolor/1.0" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xmlns:xsd="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.flowable.org/processdef">
            <process id="process_on2nfx2c" name="name_zyt7vxhb">
              <startEvent id="startNode1" name="开始">
                    <outgoing>Flow_1</outgoing>
              </startEvent>
              <userTask id="taskApply" name="申请节点[请勿删除]" flowable:assignee="\${applyUserName}">
                    <incoming>Flow_1</incoming>
              </userTask>
              <sequenceFlow id="Flow_1" sourceRef="startNode1" targetRef="taskApply" />
            </process>
            <bpmndi:BPMNDiagram id="BPMNDiagram_flow">
              <bpmndi:BPMNPlane id="BPMNPlane_flow" bpmnElement="process_on2nfx2c">
                <bpmndi:BPMNEdge id="Flow_1_di" bpmnElement="Flow_1">
                  <di:waypoint x="270" y="215" />
                  <di:waypoint x="320" y="215" />
                </bpmndi:BPMNEdge>
                <bpmndi:BPMNShape id="BPMNShape_startNode1" bpmnElement="startNode1" bioc:stroke="">
                  <omgdc:Bounds x="240" y="200" width="30" height="30" />
                  <bpmndi:BPMNLabel>
                    <omgdc:Bounds x="242" y="237" width="23" height="14" />
                  </bpmndi:BPMNLabel>
                </bpmndi:BPMNShape>
                <bpmndi:BPMNShape id="taskApply_di" bpmnElement="taskApply">
                  <omgdc:Bounds x="320" y="175" width="100" height="80" />
                </bpmndi:BPMNShape>
              </bpmndi:BPMNPlane>
            </bpmndi:BPMNDiagram>
          </definitions>
    `
}
