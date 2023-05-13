// store/WeeklyMenuStore.spec.ts
import { setActivePinia, createPinia } from 'pinia';
import { useWeeklyMenuStore } from '~/store/WeeklyMenuStore';

describe('weekly menu store', () => {
    beforeEach(() => {
        setActivePinia(createPinia());
    });

    it('sets and gets the current week recipes', () => {
        const weeklyMenuStore = useWeeklyMenuStore();
        const recipe = { id: 1, title: 'Test Recipe', ingredients: [] };

        expect(weeklyMenuStore.currentWeek[0]).toBe(null);
        weeklyMenuStore.setCurrentWeek(0, recipe);
        expect(weeklyMenuStore.currentWeek[0]).toEqual(recipe);
    });

    it('sets and gets the next week recipes', () => {
        const weeklyMenuStore = useWeeklyMenuStore();
        const recipe = { id: 1, title: 'Test Recipe', ingredients: [] };

        expect(weeklyMenuStore.nextWeek[0]).toBe(null);
        weeklyMenuStore.setNextWeek(0, recipe);
        expect(weeklyMenuStore.nextWeek[0]).toEqual(recipe);
    });

    it('sets and gets the current week locks', () => {
        const weeklyMenuStore = useWeeklyMenuStore();

        expect(weeklyMenuStore.currentWeekLocks[0]).toBe(false);
        weeklyMenuStore.setCurrentWeekLock(0, true);
        expect(weeklyMenuStore.currentWeekLocks[0]).toBe(true);
    });

    it('sets and gets the next week locks', () => {
        const weeklyMenuStore = useWeeklyMenuStore();

        expect(weeklyMenuStore.nextWeekLocks[0]).toBe(false);
        weeklyMenuStore.setNextWeekLock(0, true);
        expect(weeklyMenuStore.nextWeekLocks[0]).toBe(true);
    });

    it('sets and gets the chosen week', () => {
        const weeklyMenuStore = useWeeklyMenuStore();

        expect(weeklyMenuStore.chosenWeek).toBe(1);
        weeklyMenuStore.setChosenWeek(2);
        expect(weeklyMenuStore.chosenWeek).toBe(2);
    });

    it('sets and gets the current chosen index', () => {
        const weeklyMenuStore = useWeeklyMenuStore();

        expect(weeklyMenuStore.currentChosenIndex).toBe(null);
        weeklyMenuStore.setCurrentChosenIndex(3);
        expect(weeklyMenuStore.currentChosenIndex).toBe(3);
    });

    it('checks if current week is empty', () => {
        const weeklyMenuStore = useWeeklyMenuStore();

        expect(weeklyMenuStore.isCurrentWeekEmpty()).toBe(true);
        weeklyMenuStore.setCurrentWeek(0, { id: 1, title: 'Test Recipe', ingredients: [] });
        expect(weeklyMenuStore.isCurrentWeekEmpty()).toBe(false);
    });

    it('checks if next week is empty', () => {
        const weeklyMenuStore = useWeeklyMenuStore();

        expect(weeklyMenuStore.isNextWeekEmpty()).toBe(true);
        weeklyMenuStore.setNextWeek(0, { id: 1, title: 'Test Recipe', ingredients: [] });
        expect(weeklyMenuStore.isNextWeekEmpty()).toBe(false);
    });
});
