<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const name = ref('')
const email = ref('')
const password = ref('')
const error = ref('')
const isLoading = ref(false)

async function onSubmit() {
  error.value = ''
  isLoading.value = true
  try {
    await authStore.register({ name: name.value, email: email.value, password: password.value })
    router.push('/dashboard')
  } catch (e: unknown) {
    const err = e as { response?: { data?: { error?: string } } }
    error.value = err?.response?.data?.error ?? 'Registration failed. Please try again.'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="page">
    <div class="card">
      <h1 class="title">devboard</h1>
      <p class="subtitle">Create your account</p>

      <form @submit.prevent="onSubmit" class="form">
        <div class="field">
          <label for="name">Name</label>
          <input id="name" v-model="name" type="text" placeholder="Saurav" required />
        </div>

        <div class="field">
          <label for="email">Email</label>
          <input
            id="email"
            v-model="email"
            type="email"
            placeholder="you@example.com"
            autocomplete="email"
            required
          />
        </div>

        <div class="field">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="Min. 8 characters"
            autocomplete="new-password"
            minlength="8"
            required
          />
        </div>

        <div v-if="error" class="error">{{ error }}</div>

        <button type="submit" :disabled="isLoading" class="btn">
          {{ isLoading ? 'Creating account…' : 'Create account' }}
        </button>
      </form>

      <p class="footer-link">
        Already have an account?
        <RouterLink to="/login">Sign in</RouterLink>
      </p>
    </div>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
}
.card {
  width: 100%;
  max-width: 400px;
  padding: 2.5rem 2rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}
.title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #000;
  margin: 0 0 0.25rem;
  letter-spacing: -0.5px;
}
.subtitle {
  font-size: 0.9rem;
  color: #666;
  margin: 0 0 2rem;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}
.field {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}
label {
  font-size: 0.85rem;
  font-weight: 500;
  color: #000;
}
input {
  padding: 0.6rem 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.95rem;
  color: #000;
  background: #fff;
  outline: none;
  transition: border-color 0.15s;
}
input:focus {
  border-color: #000;
}
.error {
  font-size: 0.85rem;
  color: #c00;
  background: #fff5f5;
  border: 1px solid #fcc;
  border-radius: 4px;
  padding: 0.5rem 0.75rem;
}
.btn {
  padding: 0.65rem;
  background: #000;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: opacity 0.15s;
}
.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.btn:hover:not(:disabled) {
  opacity: 0.85;
}
.footer-link {
  margin-top: 1.5rem;
  font-size: 0.85rem;
  color: #666;
  text-align: center;
}
.footer-link a {
  color: #000;
  font-weight: 500;
  text-decoration: none;
}
.footer-link a:hover {
  text-decoration: underline;
}
</style>
