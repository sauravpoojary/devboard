import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import apiClient from '@/api/axios'

interface AuthUser {
  id: number
  email: string
  name: string
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const stored = localStorage.getItem('user')
  const user = ref<AuthUser | null>(stored ? JSON.parse(stored) : null)

  const isLoggedIn = computed(() => !!token.value)

  async function login(email: string, password: string) {
    const { data } = await apiClient.post('/api/auth/login', { email, password })
    _persist(data)
  }

  async function register(payload: { name: string; email: string; password: string }) {
    const { data } = await apiClient.post('/api/auth/register', payload)
    _persist(data)
  }

  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    router.push('/login')
  }

  function _persist(data: { id: number; token: string; email: string; name: string }) {
    token.value = data.token
    user.value = { id: data.id, email: data.email, name: data.name }
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  return { token, user, isLoggedIn, login, register, logout }
})
