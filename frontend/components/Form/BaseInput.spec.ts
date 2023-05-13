import { expect, test } from 'vitest';
import { mount } from '@vue/test-utils';
import BaseInput from './BaseInput.vue';
import LoginForm from "~/components/Form/LogInForm.vue";
import GreenButton from "~/components/Button/GreenButton.vue";
import GrayButton from "~/components/Button/GrayButton.vue";


describe('BaseInput.vue', () => {
    const wrapper = mount(BaseInput, {
        props: {
            id: 'test-id',
            label: 'Test Label',
            modelValue: 'Test Value',
            type: 'text',
            cutWidth: '70px',
        },
    });

    test('renders input box', () => {
        const input = wrapper.find('input');
        expect(input.exists()).toBe(true);
        expect(input.attributes('id')).toBe('test-id');
        expect(input.attributes('type')).toBe('text');
        expect(input.element.value).toBe('Test Value');
    });

    test('renders label', () => {
        const label = wrapper.find('label');
        expect(label.exists()).toBe(true);
        expect(label.text()).toBe('Test Label');
    });

    test('renders cut', () => {
        const label = wrapper.find('.cut');
        expect(label.exists()).toBe(true);
    });

    test('emits update:modelValue event when input value changes', async () => {
        const input = wrapper.find('input');
        await input.setValue('New Value');
        expect(wrapper.emitted('update:modelValue')).toBeTruthy();
        // @ts-ignore
        expect(wrapper.emitted('update:modelValue')[0]).toEqual(['New Value']);
    });


    test('renders input field with default type of text when type prop is not set', () => {
        wrapper.setProps({ type: undefined });
        const input = wrapper.find('input');
        expect(input.attributes('type')).toBe('text');
    });

    test('emits update:modelValue event with initiatedValue when modelValue is empty and initiatedValue is set', () => {
        const wrapper = mount(BaseInput, {
            props: {
                id: 'test-id',
                label: 'Test Label',
                modelValue: '',
                initiatedValue: 'Initiated Value',
                type: 'text',
                cutWidth: '70px',
            },
        });

        wrapper.vm.updateInitiatedValue();
        expect(wrapper.emitted('update:modelValue')).toBeTruthy();
        expect(wrapper.emitted('update:modelValue')[0]).toEqual(['Initiated Value']);
    });

});
