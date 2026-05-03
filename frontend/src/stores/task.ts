import { ref } from 'vue'
import { defineStore } from 'pinia'
import apiClient from '@/api/axios'

export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE'

export interface Task {
  id: number
  title: string
  description: string | null
  status: TaskStatus
  userId: number
  createdAt: string
  updatedAt: string
}

export interface TaskRequest {
  title: string
  description?: string
  status?: TaskStatus
}

export const useTaskStore = defineStore('task', () => {
  const tasks = ref<Task[]>([])
  const isLoading = ref(false)

  async function fetchTasks() {
    isLoading.value = true
    try {
      const { data } = await apiClient.get('/api/tasks')
      tasks.value = data
    } finally {
      isLoading.value = false
    }
  }

  async function createTask(request: TaskRequest) {
    const { data } = await apiClient.post('/api/tasks', request)
    tasks.value.unshift(data)
  }

  async function updateTask(id: number, request: TaskRequest) {
    const { data } = await apiClient.put(`/api/tasks/${id}`, request)
    const idx = tasks.value.findIndex((t) => t.id === id)
    if (idx !== -1) tasks.value[idx] = data
  }

  async function deleteTask(id: number) {
    await apiClient.delete(`/api/tasks/${id}`)
    tasks.value = tasks.value.filter((t) => t.id !== id)
  }

  return { tasks, isLoading, fetchTasks, createTask, updateTask, deleteTask }
})
