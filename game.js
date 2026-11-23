(() => {
  'use strict';
  const SAVE_KEY = 'pet-nurturing-game-save-v1';
  const $ = (id) => document.getElementById(id);
  const els = {
    name: $('pet-name'), age: $('age'), hunger: $('hunger'), happiness: $('happiness'),
    hungerMeter: $('hunger-meter'), happinessMeter: $('happiness-meter'), sheep: $('sheep'),
    speech: $('speech'), speechText: $('speech-text'), deadMessage: $('dead-message'),
    message: $('message'), feed: $('feed-btn'), play: $('play-btn'), sleep: $('sleep-btn'),
    save: $('save-btn'), reset: $('reset-btn'), nameDialog: $('name-dialog'),
    nameInput: $('name-input'), nameForm: $('name-form'), loadDialog: $('load-dialog'),
    noticeDialog: $('notice-dialog'), noticeText: $('notice-text')
  };
  let pet = null;

  const clamp = (n) => Math.max(0, Math.min(10, n));
  const isDead = () => pet.hunger <= 0 || pet.happiness <= 0;
  function showNotice(message) {
    els.noticeText.textContent = message;
    els.noticeDialog.showModal();
  }
  function newPet(name) {
    pet = { name: (name || '').trim() || 'ひつじ', hunger: 5, happiness: 5, age: 0 };
    render();
  }
  function render(message) {
    if (!pet) return;
    const dead = isDead();
    els.name.textContent = pet.name;
    els.age.textContent = pet.age;
    els.hunger.textContent = pet.hunger;
    els.happiness.textContent = pet.happiness;
    els.hungerMeter.style.width = `${pet.hunger * 10}%`;
    els.happinessMeter.style.width = `${pet.happiness * 10}%`;
    els.sheep.src = dead ? 'assets/dead.png' : 'assets/Sheep.png';
    els.sheep.alt = dead ? '天国へ行ったひつじ' : '元気なひつじ';
    els.deadMessage.classList.toggle('hidden', !dead);
    const hungry = !dead && pet.hunger <= 3;
    const lonely = !dead && !hungry && pet.happiness <= 3;
    els.speech.classList.toggle('hidden', !(hungry || lonely));
    els.speechText.textContent = hungry ? 'おなかすいた' : 'さびしいな';
    [els.feed, els.play, els.sleep].forEach((button) => button.disabled = dead);
    els.message.textContent = message || (dead ? `${pet.name}は天国へ行きました…` : 'ひつじのお世話をしてあげよう！');
  }
  function loadSaved() {
    try {
      const raw = localStorage.getItem(SAVE_KEY);
      if (!raw) return false;
      const data = JSON.parse(raw);
      if (!data || typeof data.name !== 'string' || !Number.isFinite(data.hunger) || !Number.isFinite(data.happiness) || !Number.isFinite(data.age)) return false;
      pet = { name: data.name, hunger: clamp(data.hunger), happiness: clamp(data.happiness), age: Math.max(0, data.age) };
      render('セーブデータを読み込みました！');
      return true;
    } catch (_) { return false; }
  }
  function startNew() {
    els.nameInput.value = '';
    els.nameDialog.showModal();
  }
  els.nameForm.addEventListener('submit', (event) => {
    event.preventDefault();
    const name = els.nameInput.value;
    els.nameDialog.close();
    newPet(name);
  });
  els.loadDialog.addEventListener('close', () => {
    if (els.loadDialog.returnValue === 'load') {
      if (!loadSaved()) {
        showNotice('セーブデータが見つからないため、新しく始めます。');
        startNew();
      }
    } else startNew();
  });
  els.feed.addEventListener('click', () => {
    if (isDead()) return;
    pet.hunger = clamp(pet.hunger + 3);
    render(`${pet.name}にごはんをあげました！「メェ〜！（おいしい！）」`);
  });
  els.play.addEventListener('click', () => {
    if (isDead()) return;
    if (pet.hunger > 0) {
      pet.happiness = clamp(pet.happiness + 3);
      pet.hunger = clamp(pet.hunger - 2);
    }
    render(`${pet.name}と一緒に遊びました！「メェッ、メェッ！（たのしいな！）」`);
  });
  els.sleep.addEventListener('click', () => {
    if (isDead()) return;
    pet.age += 1;
    pet.hunger = clamp(pet.hunger - 2);
    pet.happiness = clamp(pet.happiness - 1);
    render(`${pet.name}はぐっすり眠りました。1日が経過しました。`);
  });
  els.save.addEventListener('click', () => {
    if (!pet) return;
    try {
      localStorage.setItem(SAVE_KEY, JSON.stringify(pet));
      render('セーブしました！次回も続きから遊べます。');
    } catch (_) { showNotice('保存できませんでした。ブラウザの設定をご確認ください。'); }
  });
  els.reset.addEventListener('click', () => {
    if (confirm('今の状態を終了して、新しく始めますか？（未保存の進行は失われます）')) startNew();
  });

  // 初回起動時は、Java版のロード確認に近い選択画面を表示する。
  els.loadDialog.showModal();
})();
