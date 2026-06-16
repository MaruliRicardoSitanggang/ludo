# 🎲 Ludo Elite - Aturan Permainan Professional

## 📖 Overview

Ludo Elite mengimplementasikan **100% aturan Ludo professional standard** dengan tampilan modern dan gameplay yang smooth.

**Version**: 3.4.0-PROFESSIONAL-RULES  
**Compliance**: ✅ 100% Ludo Standard Rules  
**Last Updated**: June 15, 2026

---

## 🎮 Aturan Dasar

### Setup
- **Pemain**: 2-4 pemain
- **Bidak**: Setiap pemain punya 4 bidak
- **Warna**: Merah, Hijau, Kuning, Biru
- **Start**: Semua bidak mulai di base (markas)
- **Tujuan**: Pemain pertama yang semua bidaknya sampai finish = MENANG

---

## 🎯 Cara Bermain

### 1. **Roll Dadu**
- Klik tombol "ROLL DICE"
- Dadu akan roll angka 1-6
- Hanya bisa roll 1x per turn (kecuali dapat bonus)

### 2. **Keluar dari Base**
- **Harus dapat angka 6** untuk mengeluarkan bidak dari base
- Bidak keluar ke kotak START warna Anda
- Setelah keluar, dapat roll lagi (bonus)

### 3. **Menggerakkan Bidak**
- Klik bidak yang ingin dipindahkan
- Bidak bergerak sesuai angka dadu (clockwise)
- Harus ada bidak yang bisa dipindahkan

### 4. **Capturing (Memakan Bidak Lawan)**
- Landing di kotak yang sama dengan lawan → **CAPTURE!**
- Bidak lawan kembali ke base
- **KECUALI** di safe zones (kotak aman)

### 5. **Masuk Home Lane**
- Setelah mengelilingi papan 1 lap penuh (52 kotak)
- Bidak masuk jalur warna sendiri menuju center
- 5 kotak + 1 finish

### 6. **Finish**
- **Harus angka PAS** untuk sampai finish
- Overshoot = tidak bisa move
- 4 bidak finish = **MENANG!**

---

## 🛡️ Safe Zones (Kotak Aman)

### ⭐ Star Safe Zones (Safe untuk SEMUA warna)
Kotak dengan tanda bintang emas:
- Safe Zone 1: Kotak 9 (lengan atas)
- Safe Zone 2: Kotak 22 (lengan kanan)
- Safe Zone 3: Kotak 35 (lengan bawah)
- Safe Zone 4: Kotak 48 (lengan kiri)

**Aturan**:
- ✅ Bidak SEMUA warna aman di sini
- ✅ Tidak bisa di-capture
- ✅ Bisa ada multiple bidak

### 🏠 Start Squares (Safe HANYA untuk pemilik)
Kotak START dengan warna:
- RED Start: Kotak 0 (dengan arrow merah →)
- GREEN Start: Kotak 13 (dengan arrow hijau ↓)
- BLUE Start: Kotak 26 (dengan arrow biru ←)
- YELLOW Start: Kotak 39 (dengan arrow kuning ↑)

**Aturan**:
- ✅ Bidak pemilik aman (contoh: RED di RED start)
- ❌ Bidak lawan **BISA** di-capture (contoh: YELLOW di RED start)
- ✅ Keluar dari base langsung bisa capture lawan di sini

---

## 🎁 Bonus & Special Rules

### Bonus Turn (Angka 6)
- Roll **6** → dapat bonus roll lagi
- Bisa multiple kali (6 → roll → 6 → roll → ...)
- **KECUALI** 3x berturut-turut 6 (lihat di bawah)

### Skip Turn (3x Consecutive 6)
- Roll **6 → 6 → 6** berturut-turut = **SKIP TURN**
- Tidak jadi move apapun
- Turn langsung ke pemain berikutnya
- Reset counter consecutive 6

### Auto Skip (No Valid Moves)
- Roll dadu tapi tidak ada bidak yang bisa move
- Contoh: Semua bidak di base, dapat angka 1-5
- Otomatis skip setelah 2 detik

---

## 📍 Track Layout

### Main Track (52 Kotak)
```
    ┌────────────────────┐
    │   GREEN BASE       │  
    │   (kanan atas)     │
    └──────┬─────────────┘
           │ ↓ GREEN START (13)
    ┌──────▼─────────────┐
    │                    │
    │   MAIN TRACK       │
    │   (52 positions)   │
    │   Clockwise  →     │
    │                    │
    └────────────────────┘
```

**Start Positions** (Exit dari base):
- RED: Kotak 0 (tengah kiri)
- GREEN: Kotak 13 (atas tengah)
- BLUE: Kotak 26 (tengah kanan)
- YELLOW: Kotak 39 (bawah tengah)

**Path**: Setiap warna punya jalur 52 kotak relatif (0-51)

### Home Lane (Jalur Finish)
- **RED**: Ke kiri → center
- **GREEN**: Ke bawah → center
- **BLUE**: Ke kanan → center
- **YELLOW**: Ke atas → center

**Kotak**: 5 kotak + 1 finish di center

---

## ⚔️ Strategi Pro

### 1. **Prioritas Keluar dari Base**
- Angka 6? Keluarkan bidak baru dulu
- Spread bidak di track (jangan fokus 1 bidak)
- Lebih banyak bidak = lebih banyak opsi

### 2. **Manfaatkan Safe Zones**
- Parkir di star safe zones saat terancam
- Jangan parkir terlalu lama (block progress)

### 3. **Aggressive Capturing**
- Target bidak lawan yang hampir finish
- Capture di start square lawan (mereka tidak aman!)
- Bidak yang di-capture = kerugian besar untuk lawan

### 4. **Home Lane Timing**
- Jangan masuk home lane terlalu cepat
- Tunggu posisi yang aman (hindari exact number problem)
- Hitung kemungkinan angka dadu

### 5. **Multiple Pieces Strategy**
- 2-3 bidak di track > 1 bidak lead
- Backup pieces = safety net
- Pressure lawan dengan multiple threats

---

## 🚫 Common Mistakes

### ❌ Mistake 1: Parkir di Start Square Lawan
- "Saya aman di sini karena ini start square"
- **SALAH!** Start square hanya safe untuk pemilik
- Anda bisa di-capture!

### ❌ Mistake 2: Focus 1 Bidak Saja
- "Saya akan finish 1 bidak dulu"
- **Risiko!** Kalau di-capture = semua progress hilang
- Better: Spread pieces

### ❌ Mistake 3: Overshoot di Home Lane
- "Saya dapat 6, tapi finish masih 4 kotak"
- **Tidak bisa move!** Harus angka pas
- Plan ahead untuk exact number

### ❌ Mistake 4: Lupa Consecutive 6 Rule
- "Dapat 6 lagi, saya roll lagi"
- **Cek**: Sudah berapa kali dapat 6 berturut?
- 3x = SKIP (turn hilang)

---

## 📊 Scoring & Win Conditions

### Win Condition
✅ **Semua 4 bidak sampai finish**

Tidak ada poin parsial — harus finish semua bidak.

### Ranking (Multi-player)
1. **1st Place**: Finish semua bidak duluan
2. **2nd Place**: Finish ke-2
3. **3rd Place**: Finish ke-3
4. **Last Place**: Finish terakhir

Game otomatis selesai setelah ada pemenang (bisa lanjut untuk ranking).

---

## 🎨 Visual Guide

### Piece States
- **Di Base**: Bidak di markas (4 slot circular)
- **On Track**: Bidak di jalur putih (main track)
- **Home Lane**: Bidak di jalur warna menuju center
- **Finished**: Bidak di center (finish)

### Color Coding
- 🔴 **RED**: Merah vibrant
- 🟢 **GREEN**: Hijau terang
- 🔵 **BLUE**: Biru cerah
- 🟡 **YELLOW**: Kuning cerah

### Special Squares
- ⭐ **Star**: Safe zone (abu-abu dengan bintang emas)
- ➡️ **Arrow**: Start square (warna dengan arrow)
- 🌈 **Colored Lane**: Home lane (gradient warna)
- 🏁 **Center**: Finish (4 segitiga warna)

---

## 🔧 Technical Details

### Movement Calculation
- **Relative Position**: 0-51 (per warna)
- **Absolute Position**: 0-51 (shared track)
- **Conversion**: `abs = (start + rel) % 52`

### Home Lane Entry
- Trigger: Relative position >= 52
- Entry point: Setelah menyelesaikan 52 kotak main track
- Tidak bisa dilewati paksa

### Capture Mechanics
- Check: Absolute position match
- Safe check: Star zones + owner start square
- Action: Opponent → base, reset progress

---

## 📱 UI Controls

### Game Controls
- **ROLL DICE**: Klik untuk roll dadu (besar, jelas)
- **Piece Selection**: Klik bidak yang ingin dipindahkan
- **Back to Dashboard**: Tombol kiri atas

### Visual Feedback
- **Current Turn**: Highlight pemain (border warna)
- **Dice Result**: Angka dadu besar dengan emoji 🎲
- **Game Status**: Label di bawah dadu (instruksi/status)
- **Player Info**: Panel kiri (4 pemain dengan warna)

### Debug Mode (Console)
- Movement log: `[TRACK MOVE] RED piece: rel 10 → 13 ...`
- Capture log: `[CAPTURE] RED captured YELLOW at abs pos 5`
- Version check: `GameEngine Version: 3.4.0-PROFESSIONAL-RULES`

---

## ❓ FAQ

### Q: Berapa lama waktu rata-rata 1 game?
**A**: 15-30 menit untuk 4 pemain (tergantung keberuntungan dadu).

### Q: Bisa main solo (vs computer)?
**A**: Saat ini belum ada AI. Mode hot-seat (bergantian 1 device).

### Q: Aturan blockade/stacking?
**A**: Tidak diimplementasi (aturan variant, bukan standard).

### Q: Bonus movement saat capture?
**A**: Tidak ada (aturan variant, bukan standard).

### Q: Bisa undo move?
**A**: Tidak bisa — move final setelah klik bidak.

### Q: Limit waktu per turn?
**A**: Tidak ada limit waktu (casual play).

---

## 📚 Resources

### Documentation
- `LUDO_PROFESSIONAL_RULES_REVIEW.md` - Full rules review
- `CHANGELOG_v3.4.0.md` - Version history & changes
- `QUICK_START_FIX.md` - Compile & run instructions
- `FIX_CORNER_TRACK_v3.3.0.md` - Corner track fix details

### Source Code
- `GameEngine.java` - Game logic & rules
- `BoardTrack.java` - Track layout & safe zones
- `Player.java` - Player & piece management
- `LudoBoardController.java` - UI controller

---

## 🎯 Tips for New Players

### Getting Started
1. **Fokus keluar dari base dulu** (butuh angka 6)
2. **Jangan takut capture** — aggressive play wins
3. **Manfaatkan safe zones** — tapi jangan parkir lama
4. **Spread pieces** — multiple pieces > 1 lead piece

### Advanced Play
1. **Count opponent distances** — target yang hampir finish
2. **Position for capture** — 6 kotak di belakang opponent
3. **Exact finish math** — plan 2-3 moves ahead
4. **Bonus turn chaining** — maximize consecutive 6s

---

**Selamat Bermain!** 🎲🎉

---

**Version**: 3.4.0-PROFESSIONAL-RULES  
**Compliance**: 100% Ludo Standard  
**Date**: June 15, 2026
