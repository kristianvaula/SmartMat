import { expect, test } from 'vitest';
import { mount } from '@vue/test-utils';
import Wave from './Wave.vue';

test('renders Wave component', () => {
    const wrapper = mount(Wave);
    expect(wrapper.exists()).toBe(true);
});
