'use strict';

function isInArea(x, y, R) {
  const inSquare = x >= -R && x <= 0 && y >= -R && y <= 0;
  const inWedge  = x >= -R / 2 && x <= 0 && y >= 0 && y <= x + R;
  const inDisk   = x >= 0 && x <= R && y >= 0 && (x * x + y * y) <= R * R;
  return inSquare || inWedge || inDisk;
}

const canvas = document.getElementById('plane');
const ctx = canvas.getContext('2d');
const CW = canvas.width;
const CH = canvas.height;

let currentPoint = null; 

function drawPlane(R) {
  ctx.clearRect(0, 0, CW, CH);

  const margin = 40;
  const extent = R * 1.4; 
  const scale = (CW / 2 - margin) / extent;
  const originX = CW / 2;
  const originY = CH / 2;

  const px = (x) => originX + x * scale;
  const py = (y) => originY - y * scale;

  ctx.beginPath();
  ctx.moveTo(px(-R), py(-R));
  ctx.lineTo(px(0), py(-R));
  ctx.lineTo(px(0), py(0));
  ctx.lineTo(px(R), py(0));
  ctx.arc(px(0), py(0), R * scale, 0, -Math.PI / 2, true);
  ctx.lineTo(px(-R / 2), py(0));
  ctx.lineTo(px(-R), py(0));
  ctx.closePath();
  ctx.fillStyle = '#5aa0e0';
  ctx.fill();


  ctx.strokeStyle = '#222';
  ctx.fillStyle = '#222';
  ctx.lineWidth = 1;
  ctx.font = '12px sans-serif';
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';

  // x-axis
  drawArrowLine(px(-CW / 2 / scale + margin / scale), py(0), px(CW / 2 / scale - margin / scale), py(0));
  ctx.fillText('X', CW - 12, py(0) - 12);

  // y-axis
  drawArrowLine(px(0), py(-CH / 2 / scale + margin / scale), px(0), py(CH / 2 / scale - margin / scale));
  ctx.fillText('Y', px(0) + 14, 12);

  // ticks: -R, -R/2, R/2, R on both axes
  const ticks = [-R, -R / 2, R / 2, R];
  const tickLabels = ['-R', '-R/2', 'R/2', 'R'];
  ticks.forEach((t, i) => {
    // x-axis ticks
    ctx.beginPath();
    ctx.moveTo(px(t), py(0) - 4);
    ctx.lineTo(px(t), py(0) + 4);
    ctx.stroke();
    ctx.fillText(tickLabels[i], px(t), py(0) + 16);

    // y-axis ticks
    ctx.beginPath();
    ctx.moveTo(px(0) - 4, py(t));
    ctx.lineTo(px(0) + 4, py(t));
    ctx.stroke();
    ctx.textAlign = 'right';
    ctx.fillText(tickLabels[i], px(0) - 8, py(t));
    ctx.textAlign = 'center';
  });


  if (currentPoint) {
    const { x, y, inside } = currentPoint;
    ctx.beginPath();
    ctx.arc(px(x), py(y), 5, 0, Math.PI * 2);
    ctx.fillStyle = inside ? '#1e7d34' : '#c0392b';
    ctx.fill();
    ctx.strokeStyle = '#ffffff';
    ctx.lineWidth = 1.5;
    ctx.stroke();
  }
}

function drawArrowLine(x1, y1, x2, y2) {
  ctx.beginPath();
  ctx.moveTo(x1, y1);
  ctx.lineTo(x2, y2);
  ctx.stroke();
  const angle = Math.atan2(y2 - y1, x2 - x1);
  const headLen = 8;
  ctx.beginPath();
  ctx.moveTo(x2, y2);
  ctx.lineTo(x2 - headLen * Math.cos(angle - Math.PI / 6), y2 - headLen * Math.sin(angle - Math.PI / 6));
  ctx.lineTo(x2 - headLen * Math.cos(angle + Math.PI / 6), y2 - headLen * Math.sin(angle + Math.PI / 6));
  ctx.closePath();
  ctx.fill();
}

const xButtons = document.querySelectorAll('.x-btn');
const xError = document.getElementById('xError');
const yInput = document.getElementById('yInput');
const yError = document.getElementById('yError');
const rSelect = document.getElementById('rSelect');
const rError = document.getElementById('rError');
const form = document.getElementById('pointForm');

let selectedX = null;

xButtons.forEach((btn) => {
  btn.addEventListener('click', () => {
    xButtons.forEach((b) => b.classList.remove('selected'));
    btn.classList.add('selected');
    selectedX = Number(btn.dataset.value);
    xError.textContent = '';
  });
});


rSelect.addEventListener('change', () => {
  drawPlane(Number(rSelect.value));
});


yInput.addEventListener('input', () => {
  validateY(false);
});

function parseDecimal(raw) {
  
  const normalized = raw.trim().replace(',', '.');
  if (normalized === '' || normalized === '-' ) return NaN;
  if (!/^-?\d+(\.\d+)?$/.test(normalized)) return NaN;
  return Number(normalized);
}

function validateX() {
  if (selectedX === null) {
    xError.textContent = 'Выберите значение X, нажав одну из кнопок.';
    return false;
  }
  xError.textContent = '';
  return true;
}

function validateY(showEmptyError = true) {
  const raw = yInput.value;
  if (raw.trim() === '') {
    yError.textContent = showEmptyError ? 'Введите значение Y.' : '';
    return false;
  }
  const value = parseDecimal(raw);
  if (Number.isNaN(value)) {
    yError.textContent = 'Y должен быть числом (например, 1.5 или -2,3).';
    return false;
  }
  if (value < -3 || value > 3) {
    yError.textContent = 'Y должен быть в диапазоне от -3 до 3.';
    return false;
  }
  yError.textContent = '';
  return true;
}

function validateR() {
  const value = Number(rSelect.value);
  if (!Number.isInteger(value) || value < 1 || value > 5) {
    rError.textContent = 'Выберите корректное значение R.';
    return false;
  }
  rError.textContent = '';
  return true;
}


const STORAGE_KEY = 'lab1_point_results';

function loadResults() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : [];
  } catch (e) {
    return [];
  }
}

function saveResults(results) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(results));
}

function formatTimestamp(isoString) {

  const date = new Date(isoString);
  return date.toLocaleString('ru-RU', {
    dateStyle: 'short',
    timeStyle: 'medium',
  });
}

function renderResults() {
  const results = loadResults();
  const tbody = document.getElementById('resultsBody');
  const emptyState = document.getElementById('emptyState');
  tbody.innerHTML = '';

  if (results.length === 0) {
    emptyState.style.display = 'block';
  } else {
    emptyState.style.display = 'none';
  }

  results.forEach((row) => {
    const tr = document.createElement('tr');

    const tdX = document.createElement('td');
    tdX.textContent = row.x;
    const tdY = document.createElement('td');
    tdY.textContent = row.y;
    const tdR = document.createElement('td');
    tdR.textContent = row.r;
    const tdResult = document.createElement('td');
    tdResult.textContent = row.inside ? 'Попадает' : 'Не попадает';
    tdResult.className = row.inside ? 'result-inside' : 'result-outside';
    const tdDate = document.createElement('td');
    tdDate.textContent = formatTimestamp(row.timestamp);

    tr.appendChild(tdX);
    tr.appendChild(tdY);
    tr.appendChild(tdR);
    tr.appendChild(tdResult);
    tr.appendChild(tdDate);
    tbody.appendChild(tr);
  });
}


form.addEventListener('submit', (event) => {
  event.preventDefault();

  const xOk = validateX();
  const yOk = validateY(true);
  const rOk = validateR();
  if (!xOk || !yOk || !rOk) return;

  const x = selectedX;
  const y = parseDecimal(yInput.value);
  const r = Number(rSelect.value);
  const inside = isInArea(x, y, r);

  const results = loadResults();
  results.push({
    x, y, r, inside,
    timestamp: new Date().toISOString(),
  });
  saveResults(results);
  renderResults();

  currentPoint = { x, y, inside };
  drawPlane(r);
});


document.addEventListener('DOMContentLoaded', () => {
  drawPlane(Number(rSelect.value));
  renderResults();
});
