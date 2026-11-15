// ============================================================
//  Funções para mostrar datas do jeito do design
//  ("Hoje", "Amanhã", "18 nov", "Sábado, 14 de novembro"...)
// ============================================================

const MESES = ['janeiro', 'fevereiro', 'março', 'abril', 'maio', 'junho', 'julho',
  'agosto', 'setembro', 'outubro', 'novembro', 'dezembro'];
const DIAS_SEMANA = ['Domingo', 'Segunda-feira', 'Terça-feira', 'Quarta-feira',
  'Quinta-feira', 'Sexta-feira', 'Sábado'];

// Transforma '2026-11-19' em Date (sem problema de fuso horário)
export function lerData(iso: string): Date {
  const [ano, mes, dia] = iso.split('-').map(Number);
  return new Date(ano, mes - 1, dia);
}

// Transforma Date em '2026-11-19' (o formato que a API usa)
export function paraIso(data: Date): string {
  const mes = String(data.getMonth() + 1).padStart(2, '0');
  const dia = String(data.getDate()).padStart(2, '0');
  return `${data.getFullYear()}-${mes}-${dia}`;
}

export function hojeIso(): string {
  return paraIso(new Date());
}

// '2026-11-15' -> 'Hoje' | 'Amanhã' | '18 nov'
export function rotuloData(iso: string): string {
  const data = lerData(iso);
  const hoje = lerData(hojeIso());
  const diferencaDias = Math.round((data.getTime() - hoje.getTime()) / 86_400_000);
  if (diferencaDias === 0) return 'Hoje';
  if (diferencaDias === 1) return 'Amanhã';
  return `${data.getDate()} ${abreviarMes(data.getMonth())}`;
}

// 'nov', 'dez'...
export function abreviarMes(mes: number): string {
  return MESES[mes].slice(0, 3);
}

// 'Sábado, 14 de novembro'
export function dataPorExtenso(data: Date): string {
  return `${DIAS_SEMANA[data.getDay()]}, ${data.getDate()} de ${MESES[data.getMonth()]}`;
}

// 'Novembro 2026'
export function mesEAno(data: Date): string {
  const mes = MESES[data.getMonth()];
  return `${mes[0].toUpperCase()}${mes.slice(1)} ${data.getFullYear()}`;
}
