import { setActivePinia, createPinia } from 'pinia'
import { useRefrigeratorStore } from '~/store/refrigeratorStore'
import { useUserStore } from '~/store/userStore'

describe('refrigerator store', () => {
    beforeEach(() => {
        setActivePinia(createPinia())
    })

    it('sets selected refrigerator when setSelectedRefrigerator is called with valid refrigerator', () => {
        const refrigeratorStore = useRefrigeratorStore()
        refrigeratorStore.setRefrigerators([{id: 1}, {id: 2}])
        expect(refrigeratorStore.getSelectedRefrigerator).toBeNull()

        // Select the first refrigerator
        const firstRefrigerator = refrigeratorStore.getRefrigerators[0]
        refrigeratorStore.setSelectedRefrigerator(firstRefrigerator)
        expect(refrigeratorStore.getSelectedRefrigerator).toEqual(firstRefrigerator)

        // Select the second refrigerator
        const secondRefrigerator = refrigeratorStore.getRefrigerators[1]
        refrigeratorStore.setSelectedRefrigerator(secondRefrigerator)
        expect(refrigeratorStore.getSelectedRefrigerator).toEqual(secondRefrigerator)
    })

    it('does not select a refrigerator if setSelectedRefrigerator is called with an invalid refrigerator', () => {
        const refrigeratorStore = useRefrigeratorStore()
        refrigeratorStore.setRefrigerators([{id: 1}, {id: 2}])
        expect(refrigeratorStore.getSelectedRefrigerator).toBeNull()

        // Attempt to select a non-existent refrigerator
        const nonExistentRefrigerator = {id: 3}
        const setSelectedRefrigeratorResult = refrigeratorStore.setSelectedRefrigerator(nonExistentRefrigerator)
        expect(setSelectedRefrigeratorResult).toBe(false)
        expect(refrigeratorStore.getSelectedRefrigerator).toBeNull()
    })

    it('does not select a refrigerator if setSelectedRefrigerator is called when no refrigerators are available', () => {
        const refrigeratorStore = useRefrigeratorStore()
        refrigeratorStore.setRefrigerators([])
        expect(refrigeratorStore.getSelectedRefrigerator).toBeNull()

        // Attempt to select a refrigerator
        const setSelectedRefrigeratorResult = refrigeratorStore.setSelectedRefrigerator({id: 1})
        expect(setSelectedRefrigeratorResult).toBe(false)
        expect(refrigeratorStore.getSelectedRefrigerator).toBeNull()
    })


    it('returns all refrigerators when getRefrigerators is called', () => {
        const refrigeratorStore = useRefrigeratorStore();
        const refrigerators = [{ id: 1 }, { id: 2 }];

        refrigeratorStore.setRefrigerators(refrigerators);

        expect(refrigeratorStore.getRefrigerators).toEqual(refrigerators);
    });

    it('returns a refrigerator by id when getRefrigeratorById is called', () => {
        const refrigeratorStore = useRefrigeratorStore();
        const refrigerators = [{ id: 1 }, { id: 2 }];

        refrigeratorStore.setRefrigerators(refrigerators);

        expect(refrigeratorStore.getRefrigeratorById(1)).toEqual({ id: 1 });
        expect(refrigeratorStore.getRefrigeratorById(2)).toEqual({ id: 2 });
    });

    it('does not return a refrigerator by id when getRefrigeratorById is called with an invalid id', () => {
        const refrigeratorStore = useRefrigeratorStore();
        const refrigerators = [{ id: 1 }, { id: 2 }];

        refrigeratorStore.setRefrigerators(refrigerators);

        expect(refrigeratorStore.getRefrigeratorById(3)).toBeUndefined();
    });

    it('does not automatically select a refrigerator when the user is not logged in', () => {
        const refrigeratorStore = useRefrigeratorStore();

        refrigeratorStore.setRefrigerators([{ id: 1 }]);

        expect(refrigeratorStore.getSelectedRefrigerator).toBeNull();
    });


})