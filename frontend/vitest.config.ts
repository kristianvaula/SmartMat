import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
    plugins: [vue()],
    test: {
        environment: 'jsdom',
        globals: true,
        coverage: {
            all: true,
            provider: 'c8',
            include: ['components/**/*.vue', 'store/**/*.ts'],
        }
    },
    resolve: {
        alias: {
            '~': __dirname,
        },
    },
    logLevel: 'warn',
})
