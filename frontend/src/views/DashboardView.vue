<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useTaskStore, type TaskStatus } from '@/stores/task'
import AppLayout from '@/components/AppLayout.vue'

const taskStore = useTaskStore()

const showForm = ref(false)
const newTitle = ref('')
const newDescription = ref('')
const newStatus = ref<TaskStatus>('TODO')
const formError = ref('')
const isSubmitting = ref(false)

const editingId = ref<number | null>(null)
const editTitle = ref('')
const editDescription = ref('')
const editStatus = ref<TaskStatus>('TODO')

const filterStatus = ref<TaskStatus | 'ALL'>('ALL')

const filteredTasks = computed(() => {
  if (filterStatus.value === 'ALL') return taskStore.tasks
  return taskStore.tasks.filter((t) => t.status === filterStatus.value)
})

const statusLabel: Record<TaskStatus, string> = {
  TODO: 'To Do',
  IN_PROGRESS: 'In Progress',
  DONE: 'Done',
}

onMounted(() => taskStore.fetchTasks())

async function submitCreate() {
  if (!newTitle.value.trim()) {
    formError.value = 'Title is required'
    return
  }
  formError.value = ''
  isSubmitting.value = true
  try {
    await taskStore.createTask({
      title: newTitle.value.trim(),
      description: newDescription.value.trim() || undefined,
      status: newStatus.value,
    })
    newTitle.value = ''
    newDescription.value = ''
    newStatus.value = 'TODO'
    showForm.value = false
  } catch {
    formError.value = 'Failed to create task'
  } finally {
    isSubmitting.value = false
  }
}

function startEdit(task: { id: number; title: string; description: string | null; status: TaskStatus }) {
  editingId.value = task.id
  editTitle.value = task.title
  editDescription.value = task.description ?? ''
  editStatus.value = task.status
}

async function submitEdit() {
  if (!editTitle.value.trim() || editingId.value === null) return
  try {
    await taskStore.updateTask(editingId.value, {
      title: editTitle.value.trim(),
      description: editDescription.value.trim() || undefined,
      status: editStatus.value,
    })
    editingId.value = null
  } catch {
    // silently fail — could add toast here
  }
}

async function remove(id: number) {
  if (confirm('Delete this task?')) {
    await taskStore.deleteTask(id)
  }
}
</script>

<template>
  <AppLayout>
    <div class="page-header">
      <h2 class="page-title">Tasks</h2>
      <button class="btn-primary" @click="showForm = !showForm">
        {{ showForm ? 'Cancel' : '+ New task' }}
      </button>
    </div>

    <!-- Create form -->
    <div v-if="showForm" class="form-card">
      <div class="field">
        <label for="new-title">Title</label>
        <input id="new-title" v-model="newTitle" type="text" placeholder="Task title" />
      </div>
      <div class="field">
        <label for="new-desc">Description</label>
        <input id="new-desc" v-model="newDescription" type="text" placeholder="Optional description" />
      </div>
      <div class="field">
        <label for="new-status">Status</label>
        <select id="new-status" v-model="newStatus">
          <option value="TODO">To Do</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="DONE">Done</option>
        </select>
      </div>
      <div v-if="formError" class="error">{{ formError }}</div>
      <button class="btn-primary" :disabled="isSubmitting" @click="submitCreate">
        {{ isSubmitting ? 'Creating…' : 'Create task' }}
      </button>
    </div>

    <!-- Filter tabs -->
    <div class="filter-tabs">
      <button
        v-for="f in ['ALL', 'TODO', 'IN_PROGRESS', 'DONE']"
        :key="f"
        class="tab"
        :class="{ active: filterStatus === f }"
        @click="filterStatus = f as TaskStatus | 'ALL'"
      >
        {{ f === 'ALL' ? 'All' : statusLabel[f as TaskStatus] }}
      </button>
    </div>

    <!-- Loading -->
    <div v-if="taskStore.isLoading" class="empty">Loading…</div>

    <!-- Empty state -->
    <div v-else-if="filteredTasks.length === 0" class="empty">No tasks yet.</div>

    <!-- Task list -->
    <div v-else class="task-list">
      <div v-for="task in filteredTasks" :key="task.id" class="task-card">
        <!-- View mode -->
        <template v-if="editingId !== task.id">
          <div class="task-main">
            <span class="task-title">{{ task.title }}</span>
            <span class="badge" :class="task.status.toLowerCase().replace('_', '-')">
              {{ statusLabel[task.status] }}
            </span>
          </div>
          <p v-if="task.description" class="task-desc">{{ task.description }}</p>
          <div class="task-actions">
            <button class="btn-ghost" @click="startEdit(task)">Edit</button>
            <button class="btn-ghost danger" @click="remove(task.id)">Delete</button>
          </div>
        </template>

        <!-- Edit mode -->
        <template v-else>
          <div class="field">
            <label :for="`edit-title-${task.id}`">Title</label>
            <input :id="`edit-title-${task.id}`" v-model="editTitle" type="text" placeholder="Title" />
          </div>
          <div class="field">
            <label :for="`edit-desc-${task.id}`">Description</label>
            <input :id="`edit-desc-${task.id}`" v-model="editDescription" type="text" placeholder="Description" />
          </div>
          <div class="field">
            <label :for="`edit-status-${task.id}`">Status</label>
            <select :id="`edit-status-${task.id}`" v-model="editStatus">
              <option value="TODO">To Do</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="DONE">Done</option>
            </select>
          </div>
          <div class="task-actions">
            <button class="btn-primary small" @click="submitEdit">Save</button>
            <button class="btn-ghost" @click="editingId = null">Cancel</button>
          </div>
        </template>
      </div>
    </div>
  </AppLayout>
</template>

<style scoped>
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.5rem;
}
.page-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #000;
  margin: 0;
}
.form-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.25rem;
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.field {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}
label {
  font-size: 0.8rem;
  font-weight: 500;
  color: #444;
}
input,
select {
  padding: 0.55rem 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.9rem;
  color: #000;
  background: #fff;
  outline: none;
}
input:focus,
select:focus {
  border-color: #000;
}
.error {
  font-size: 0.82rem;
  color: #c00;
}
.filter-tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.25rem;
}
.tab {
  padding: 0.35rem 0.85rem;
  border: 1px solid #ccc;
  border-radius: 20px;
  font-size: 0.82rem;
  background: #fff;
  color: #666;
  cursor: pointer;
  transition: all 0.15s;
}
.tab.active {
  background: #000;
  color: #fff;
  border-color: #000;
}
.empty {
  color: #999;
  font-size: 0.9rem;
  padding: 2rem 0;
  text-align: center;
}
.task-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}
.task-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1rem 1.25rem;
}
.task-main {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}
.task-title {
  font-size: 0.95rem;
  font-weight: 500;
  color: #000;
}
.task-desc {
  font-size: 0.85rem;
  color: #666;
  margin: 0.35rem 0 0;
}
.badge {
  font-size: 0.75rem;
  padding: 0.2rem 0.6rem;
  border-radius: 20px;
  white-space: nowrap;
  font-weight: 500;
}
.badge.todo {
  background: #f5f5f5;
  color: #555;
}
.badge.in-progress {
  background: #000;
  color: #fff;
}
.badge.done {
  background: #e8f5e9;
  color: #2e7d32;
}
.task-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.75rem;
}
.btn-primary {
  padding: 0.55rem 1.1rem;
  background: #000;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 0.88rem;
  font-weight: 500;
  cursor: pointer;
  transition: opacity 0.15s;
}
.btn-primary.small {
  padding: 0.35rem 0.85rem;
  font-size: 0.82rem;
}
.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.btn-primary:hover:not(:disabled) {
  opacity: 0.85;
}
.btn-ghost {
  padding: 0.35rem 0.85rem;
  background: transparent;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.82rem;
  color: #444;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-ghost:hover {
  background: #f5f5f5;
}
.btn-ghost.danger {
  color: #c00;
  border-color: #fcc;
}
.btn-ghost.danger:hover {
  background: #fff5f5;
}
</style>
