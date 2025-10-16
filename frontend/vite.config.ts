import { defineConfig } from "vite";

export default defineConfig({
    base: "./",
    build: {
        rollupOptions: {
            input: {
                main: 'index.html',
                restaurant: 'restaurantPage.html',
                checkout: 'checkOutPage.html',
                orderTracking: 'orderTracking.html'
            }
        }
       // outDir: "../backend/src/main/resources/static",
    },
    // to deal with deprecation warnings until bootstrap moves to @use with v6(?)
    css: {
        preprocessorOptions: {
            scss: {
                api: 'modern-compiler', // or "modern"
                silenceDeprecations: ['mixed-decls', 'color-functions', 'global-builtin', 'import']
            }
        }
    }
});