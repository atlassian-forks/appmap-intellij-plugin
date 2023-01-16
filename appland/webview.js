import Vue from 'vue';
import '@appland/diagrams/dist/style.css';
import {VVsCodeExtension} from '@appland/components';
import MessagePublisher from './messagePublisher';
import vscode from './vsCodeBridge';

export function mountWebview() {
  const messages = new MessagePublisher(vscode);

  let app = new Vue(
      {
        el: '#app',
        render: (h) => h(VVsCodeExtension, {ref: 'ui', props: {appMapUploadable: false}}),
        // "ready" activates the Java host, which then sends the init message
        mounted: () => vscode.postMessage({command: 'ready'}),
        methods: {
          async loadAppMap(jsonString) {
            try {
              const appmapData = JSON.parse(jsonString);
              this.$refs.ui.loadData(appmapData);
            } catch (e) {
              console.error("error parsing JSON", e);
            }
          },
          async setState(jsonString) {
            try {
              this.$refs.ui.setState(jsonString);
            } catch (e) {
              console.error("error setting state: " + jsonString, e);
            }
          },
          showInstructions() {
            this.$refs.ui.showInstructions();
          },
        }
      }
  )

  messages.on('init', ({data: initData}) => {
    // messages emitted by the webview
    app.$on('viewSource', (location) => vscode.postMessage({command: 'view-source', location}))
    app.$on('uploadAppmap', () => vscode.postMessage({command: 'uploadAppMap'}))
    app.$on('request-resolve-location', (location) => {
      app.$emit('response-resolve-location', {location, externalUrl: location});
    });

    // messages emitted by the Java host
    messages.on('loadAppMap', ({data}) => app.loadAppMap(data))
    messages.on('setAppMapState', (json) => app.setState(json.state))
    messages.on('showAppMapInstructions', () => app.showInstructions())

    // Load the AppMap data into the web view
    app.loadAppMap(initData);
  })
}
