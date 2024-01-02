import  InteractionEventsModule from 'diagram-js/lib/features/interaction-events'
import OverlaysModule from 'diagram-js/lib/features/overlays';

import ContextPad from './ContextPad';


export default {
  __depends__: [
    InteractionEventsModule,
    OverlaysModule
  ],
  contextPad: [ 'type', ContextPad ]
};