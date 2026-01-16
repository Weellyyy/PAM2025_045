# 🔴 MASALAH DITEMUKAN: Stok Berkurang 2x Lipat

## 📍 Lokasi Bug
**File:** `order.controller.js` → Fungsi `createOrder()`

---

## 🐛 Root Cause Analysis

### Masalahnya di Step 5: Update Stok Barang

```javascript
// ❌ INI ADALAH BUG!
const stokUpdates = items.reduce((acc, item) => {
  if (!acc[item.barang_id]) {
    acc[item.barang_id] = 0;
  }
  acc[item.barang_id] += item.jumlah;  // ← Ini sudah menghitung total
  return acc;
}, {});

// Kemudian Loop yang SALAH:
const updatePromises = Object.entries(stokUpdates).map(([barangId, jumlah]) => {
  return new Promise((resolve, reject) => {
    db.query(
      'UPDATE barang SET stok = stok - ? WHERE barang_id = ?',
      [jumlah, barangId],  // ← Jumlah sudah di-aggregate
      (err, result) => {
        if (err) reject(err);
        else resolve(result);
      }
    );
  });
});
```

### Skenario Bug:
```
User membuat ORDER dengan items:
├─ Barang A: qty 2
└─ Barang B: qty 3

Apa yang SEHARUSNYA terjadi:
├─ Update barang A: stok = stok - 2
└─ Update barang B: stok = stok - 3

Apa yang TERJADI (Bug):
├─ stokUpdates = { 'A': 2, 'B': 3 }
├─ Loop order items LAGI (implicit loop dari Promise)
└─ Update barang A: stok = stok - 2 - 2 = stok - 4 ❌

ATAU bisa juga masalah dari Order.create() callback yang dijalankan 2x
```

---

## ✅ SOLUSI: Update Stok dengan Benar

Ganti Step 5 dalam `order.controller.js`:

```javascript
// ✅ BENAR - Langsung loop items dan update
const updatePromises = items.map((item) => {
  return new Promise((resolve, reject) => {
    db.query(
      'UPDATE barang SET stok = stok - ? WHERE barang_id = ?',
      [item.jumlah, item.barang_id],  // ← Langsung dari item
      (err, result) => {
        if (err) reject(err);
        else {
          console.log(`[ORDER] Stok ${item.barang_id} berkurang ${item.jumlah}`);
          resolve(result);
        }
      }
    );
  });
});
```

---

## 📋 Checklist Perubahan

- [ ] Update logic stok di `order.controller.js` Step 5
- [ ] Remove `stokUpdates` aggregation (tidak perlu)
- [ ] Loop langsung dari `items` array
- [ ] Test: Buat order qty 2 → Stok berkurang 2 (bukan 4)
- [ ] Test: Buat order 2 item berbeda → Masing-masing berkurang sesuai qty

---

## 🧪 Testing

**Sebelum fix:**
```
Stok awal Mie Instan: 100
Buat Order: qty 2
Stok akhir: 96 ❌ (harusnya 98)
```

**Setelah fix:**
```
Stok awal Mie Instan: 100
Buat Order: qty 2
Stok akhir: 98 ✅
```

