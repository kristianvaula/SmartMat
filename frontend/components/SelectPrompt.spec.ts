import { mount } from '@vue/test-utils';
import { expect, test } from 'vitest';
import SelectPrompt from './SelectPrompt.vue';

describe('SelectPrompt', () => {
    const refrigerators = [
        { id: 1, name: 'Refrigerator 1' },
        { id: 2, name: 'Refrigerator 2' },
        { id: 3, name: 'Refrigerator 3' },
    ];

    test('displays the list of refrigerators', () => {
        const wrapper = mount(SelectPrompt, {
            props: {
                refrigerators,
            },
        });

        const items = wrapper.findAll('li');
        expect(items).toHaveLength(3);
        expect(items[0].text()).toBe('Refrigerator 1');
        expect(items[1].text()).toBe('Refrigerator 2');
        expect(items[2].text()).toBe('Refrigerator 3');
    });

    test('emits a "clicked" event when an item is clicked', async () => {
        const wrapper = mount(SelectPrompt, {
            props: {
                refrigerators,
            },
        });

        const items = wrapper.findAll('li');
        await items[1].trigger('click');

        expect(wrapper.emitted('clicked')).toBeTruthy();
        expect(wrapper.emitted('clicked')).toHaveLength(1);
        expect(wrapper.emitted('clicked')[0]).toEqual([refrigerators[1]]);
    });
});
