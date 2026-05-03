<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js'
import apiClient from '@/api/axios'
import AppLayout from '@/components/AppLayout.vue'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

interface Stats {
  total: number
  todo: number
  inProgress: number
  done: number
}

const stats = ref<Stats | null>(null)
const isLoading = ref(true)
const error = ref('')

const chartData = ref({
  labels: ['To Do', 'In Progress', 'Done'],
  datasets: [
    {
      label: 'Tasks',
      data: [0, 0, 0],
      backgroundColor: ['#e0e0e0', '#000000', '#4caf50'],
      borderRadius: 4,
    },
  ],
})

const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: false },
    title: { display: false },
  },
  scales: {
    y: {
      beginAtZero: true,
      ticks: { stepSize: 1 },
      grid: { color: '#f0f0f0' },
    },
    x: {
      grid: { display: false },
    },
  },
}

onMounted(async () => {
  try {
    const { data } = await apiClient.get('/api/stats')
    stats.value = data
    chartData.value.datasets[0]!.data = [data.todo, data.inProgress, data.done]
  } catch {
    error.value = 'Failed to load stats'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <AppLayout>
    <div class="page-header">
      <h2 class="page-title">Stats</h2>
    </div>

    <div v-if="isLoading" class="empty">Loading…</div>
    <div v-else-if="error" class="error">{{ error }}</div>

    <template v-else-if="stats">
      <!-- Summary cards -->
      <div class="stat-grid">
        <div class="stat-card">
          <span class="stat-value">{{ stats.total }}</span>
          <span class="stat-label">Total</span>
        </div>
        <div class="stat-card">
          <span class="stat-value">{{ stats.todo }}</span>
          <span class="stat-label">To Do</span>
        </div>
        <div class="stat-card highlight">
          <span class="stat-value">{{ stats.inProgress }}</span>
          <span class="stat-label">In Progress</span>
        </div>
        <div class="stat-card done">
          <span class="stat-value">{{ stats.done }}</span>
          <span class="stat-label">Done</span>
        </div>
      </div>

      <!-- Bar chart -->
      <div class="chart-card">
        <h3 class="chart-title">Task breakdown</h3>
        <Bar :data="chartData" :options="chartOptions" />
      </div>
    </template>
  </AppLayout>
</template>

<style scoped>
.page-header {
  margin-bottom: 1.5rem;
}
.page-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #000;
  margin: 0;
}
.empty {
  color: #999;
  font-size: 0.9rem;
  padding: 2rem 0;
  text-align: center;
}
.error {
  font-size: 0.85rem;
  color: #c00;
}
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1rem;
  margin-bottom: 2rem;
}
.stat-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}
.stat-card.highlight {
  background: #000;
  border-color: #000;
}
.stat-card.highlight .stat-value,
.stat-card.highlight .stat-label {
  color: #fff;
}
.stat-card.done {
  background: #f1f8f1;
  border-color: #c8e6c9;
}
.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: #000;
  line-height: 1;
}
.stat-label {
  font-size: 0.8rem;
  color: #666;
}
.chart-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.5rem;
}
.chart-title {
  font-size: 0.9rem;
  font-weight: 600;
  color: #000;
  margin: 0 0 1.25rem;
}
</style>
