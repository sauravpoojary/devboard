<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
</script>

<template>
  <div class="layout">
    <header class="header">
      <RouterLink to="/dashboard" class="brand">devboard</RouterLink>
      <nav class="nav">
        <RouterLink to="/dashboard" class="nav-link">Tasks</RouterLink>
        <RouterLink to="/stats" class="nav-link">Stats</RouterLink>
      </nav>
      <div class="user-area">
        <span class="user-name">{{ authStore.user?.name }}</span>
        <button class="logout-btn" @click="authStore.logout()">Logout</button>
      </div>
    </header>
    <main class="main">
      <slot />
    </main>
  </div>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  background: #fff;
}
.header {
  display: flex;
  align-items: center;
  gap: 2rem;
  padding: 0 2rem;
  height: 56px;
  border-bottom: 1px solid #e0e0e0;
}
.brand {
  font-size: 1.1rem;
  font-weight: 700;
  color: #000;
  text-decoration: none;
  letter-spacing: -0.5px;
}
.nav {
  display: flex;
  gap: 1.5rem;
  flex: 1;
}
.nav-link {
  font-size: 0.9rem;
  color: #666;
  text-decoration: none;
  transition: color 0.15s;
}
.nav-link:hover,
.nav-link.router-link-active {
  color: #000;
  font-weight: 500;
}
.user-area {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.user-name {
  font-size: 0.85rem;
  color: #444;
}
.logout-btn {
  font-size: 0.85rem;
  padding: 0.35rem 0.85rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  background: #fff;
  color: #000;
  cursor: pointer;
  transition: background 0.15s;
}
.logout-btn:hover {
  background: #f5f5f5;
}
.main {
  padding: 2rem;
  max-width: 900px;
  margin: 0 auto;
}
</style>
