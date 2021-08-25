import Vue from 'vue';
import { default as plugin } from '@appland/components'; // eslint-disable-line import/no-named-default

export {
  mountQuickstartWelcome,
  mountQuickstartInstallAgent,
  mountQuickstartOpenAppmaps,
  mountQuickstartRecordAppmaps,
} from './quickstartDocsView';

Vue.use(plugin);
