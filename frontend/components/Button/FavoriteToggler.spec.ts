import { mount } from '@vue/test-utils';
import FavoriteButton from '~/components/Button/FavoriteToggler.vue';

describe('FavoriteButton', () => {
    it('renders add favorite button when isFavorite prop is false', () => {
        const wrapper = mount(FavoriteButton, {
            props: {
                isFavorite: false,
            },
            global: {
                mocks: {
                    $t: (key) => key,
                },
            },
        });
        expect(wrapper.find('img').attributes().src).toBe('/assets/icons/heart.png');
        expect(wrapper.find('h2').text()).toBe('add_favorite');
    });

    it('renders remove favorite button when isFavorite prop is true', () => {
        const wrapper = mount(FavoriteButton, {
            props: {
                isFavorite: true,
            },
            global: {
                mocks: {
                    $t: (key) => key,
                },
            },
        });
        expect(wrapper.find('img').attributes().src).toBe('/assets/icons/heartfilled.png');
        expect(wrapper.find('h2').text()).toBe('remove_favorite');
    });

    it('emits "favoriteEvent" event when clicked', async () => {
        const wrapper = mount(FavoriteButton, {
            props: {
                isFavorite: false,
            },
            global: {
                mocks: {
                    $t: (key) => key,
                },
            },
        });
        await wrapper.find('div').trigger('click');
        expect(wrapper.emitted('favoriteEvent')).toBeTruthy();
        expect(wrapper.emitted('favoriteEvent')[0]).toEqual([true]);
    });
});