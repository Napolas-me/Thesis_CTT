// Utility functions for formatting data
export const formatDate = (date: Date): string => {
  return date.toLocaleDateString('pt-PT');
};

export const formatTime = (date: Date): string => {
  return date.toLocaleTimeString('pt-PT', { 
    hour: '2-digit', 
    minute: '2-digit' 
  });
};

export const formatDateTime = (date: Date): string => {
  return `${formatDate(date)} ${formatTime(date)}`;
};

export const formatTimeUntil = (targetDate: Date): string => {
  const now = new Date();
  const diff = targetDate.getTime() - now.getTime();
  
  if (diff <= 0) return 'Expirado';
  
  const minutes = Math.floor(diff / (1000 * 60));
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  
  if (days > 0) {
    return `${days}d ${hours % 24}h`;
  } else if (hours > 0) {
    return `${hours}h ${minutes % 60}m`;
  } else {
    return `${minutes}m`;
  }
};

export const formatCapacityPercentage = (current: number, total: number): string => {
  const percentage = Math.round((current / total) * 100);
  return `${percentage}%`;
};

export const formatParcelId = (id: string): string => {
  return id.toUpperCase();
};

export const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('pt-PT', {
    style: 'currency',
    currency: 'EUR'
  }).format(amount);
};