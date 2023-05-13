// stores/notification.spec.ts
import { setActivePinia, createPinia } from 'pinia'
import { useNotificationStore } from '~/store/notificationStore'

describe('notification store', () => {
    beforeEach(() => {
        setActivePinia(createPinia())
    })

    it('sets notifications when setNotification is called', () => {
        const notificationStore = useNotificationStore()
        expect(notificationStore.getNotifications).toHaveLength(0)
        const notifications = [      { id: 1, message: 'Notification 1', refrigeratorGrocery: { id: 1, physicalExpireDate: '2023-06-01' } },      { id: 2, message: 'Notification 2', refrigeratorGrocery: { id: 2, physicalExpireDate: '2023-06-02' } },    ]
        notificationStore.setNotification(notifications)
        expect(notificationStore.getNotifications).toEqual(notifications)
    })

    it('deletes a notification when deleteNotification is called', () => {
        const notificationStore = useNotificationStore()
        const notifications = [      { id: 1, message: 'Notification 1', refrigeratorGrocery: { id: 1, physicalExpireDate: '2023-06-01' } },      { id: 2, message: 'Notification 2', refrigeratorGrocery: { id: 2, physicalExpireDate: '2023-06-02' } },    ]
        notificationStore.setNotification(notifications)

        const notificationToDelete = notifications[0]
        notificationStore.deleteNotification(notificationToDelete)

        expect(notificationStore.getNotifications).not.toContain(notificationToDelete)
    })

})
