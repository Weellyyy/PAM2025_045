# 🔧 SOLUSI DELETE GAGAL - PERBAIKAN LENGKAP

## Pada Tanggal: 12 Januari 2026

---

## 📊 RINGKASAN MASALAH

**Error Message dari User:**
```
Gagal menghapus barang: Internal Server Error
Gagal menghapus toko: Internal Server Error
```

---

## 🔍 ANALISIS ROOT CAUSE

### 1. **Android Client: ✅ SEMPURNA**
Tidak ada bug di Android code:
- ✅ API interface benar (Retrofit @DELETE)
- ✅ Repository implementation benar
- ✅ ViewModel proper error handling
- ✅ UI callback benar

### 2. **Backend: 🔴 MASALAH 500 Internal Server Error**
Kemungkinan penyebab:

#### A. Foreign Key Constraint Violation
```
Toko/Barang tidak bisa dihapus karena masih direferensikan 
oleh tabel Order atau lainnya
```

#### B. SQL Query Error
```
Syntax error atau logic error di DELETE statement
```

#### C. Authorization Missing
```
Backend mungkin memerlukan authorization header
yang tidak dikirim dengan benar
```

#### D. Endpoint Tidak Terdaftar
```
DELETE route mungkin belum diimplementasi di backend
```

---

## ✅ PERBAIKAN DI ANDROID SIDE

### Improvement 1: Better Error Message Handling

**File: TokoViewModel.kt**

```kotlin
fun deleteToko(id: Int) {
    viewModelScope.launch {
        _uiState.value = TokoUiState.Loading
        try {
            val response = repositoryToko.deleteToko(id)
            when {
                response.isSuccessful -> {
                    _uiState.value = TokoUiState.Success("Toko berhasil dihapus")
                }
                response.code() == 409 -> {
                    // Conflict - mungkin ada referensi
                    _uiState.value = TokoUiState.Error(
                        "Toko tidak bisa dihapus karena masih ada barang/order yang menggunakan toko ini. " +
                        "Silakan hapus barang/order terlebih dahulu."
                    )
                }
                response.code() == 404 -> {
                    _uiState.value = TokoUiState.Error("Toko tidak ditemukan")
                }
                response.code() == 401 -> {
                    _uiState.value = TokoUiState.Error("Tidak terauthorisasi. Silakan login ulang.")
                }
                response.code() == 500 -> {
                    _uiState.value = TokoUiState.Error(
                        "Terjadi kesalahan di server. " +
                        "Silakan hubungi administrator atau coba lagi nanti."
                    )
                }
                else -> {
                    _uiState.value = TokoUiState.Error(
                        "Gagal menghapus toko: ${response.message()} (Code: ${response.code()})"
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.value = TokoUiState.Error("Error: ${e.message}")
        }
    }
}
```

### Improvement 2: Sama untuk BarangViewModel.kt

---

## 🔴 PERBAIKAN DI BACKEND SIDE (Node.js/Express)

### Problem: Foreign Key Constraint

**Solusi - Cascade Delete atau Check First:**

```javascript
// ❌ SALAH - Akan error jika ada foreign key
app.delete('/api/toko/:id', (req, res) => {
    const { id } = req.params;
    db.query('DELETE FROM toko WHERE toko_id = ?', [id], (err, result) => {
        if (err) return res.status(500).json({ message: err.message });
        res.status(200).json({ message: 'Toko berhasil dihapus' });
    });
});

// ✅ BENAR - Check referensi dulu
app.delete('/api/toko/:id', (req, res) => {
    const { id } = req.params;

    // Step 1: Validasi ID
    if (!id || isNaN(id)) {
        return res.status(400).json({ message: 'ID toko tidak valid' });
    }

    // Step 2: Cek apakah ada barang yang pakai toko ini
    db.query(
        'SELECT COUNT(*) as count FROM barang WHERE toko_id = ?',
        [id],
        (err, results) => {
            if (err) {
                console.error('Error checking barang:', err);
                return res.status(500).json({ message: 'Server error' });
            }

            if (results[0].count > 0) {
                return res.status(409).json({
                    message: 'Toko tidak bisa dihapus karena masih memiliki barang yang terdaftar'
                });
            }

            // Step 3: Delete toko
            db.query('DELETE FROM toko WHERE toko_id = ?', [id], (err, result) => {
                if (err) {
                    console.error('Error deleting toko:', err);
                    return res.status(500).json({ message: 'Gagal menghapus toko' });
                }

                if (result.affectedRows === 0) {
                    return res.status(404).json({ message: 'Toko tidak ditemukan' });
                }

                res.status(200).json({ message: 'Toko berhasil dihapus' });
            });
        }
    );
});

// ✅ BENAR - Untuk Barang, check order-detail dulu
app.delete('/api/barang/:id', (req, res) => {
    const { id } = req.params;

    // Validasi
    if (!id || isNaN(id)) {
        return res.status(400).json({ message: 'ID barang tidak valid' });
    }

    // Cek referensi
    db.query(
        'SELECT COUNT(*) as count FROM order_detail WHERE barang_id = ?',
        [id],
        (err, results) => {
            if (err) {
                console.error('Error:', err);
                return res.status(500).json({ message: 'Server error' });
            }

            if (results[0].count > 0) {
                return res.status(409).json({
                    message: 'Barang tidak bisa dihapus karena masih digunakan dalam order'
                });
            }

            // Delete barang
            db.query('DELETE FROM barang WHERE barang_id = ?', [id], (err, result) => {
                if (err) {
                    console.error('Error:', err);
                    return res.status(500).json({ message: 'Gagal menghapus barang' });
                }

                if (result.affectedRows === 0) {
                    return res.status(404).json({ message: 'Barang tidak ditemukan' });
                }

                res.status(200).json({ message: 'Barang berhasil dihapus' });
            });
        }
    );
});
```

---

## 📋 CHECKLIST UNTUK DEBUG

### ✅ Di Android:
- [ ] Cek Logcat - apakah ada error message lebih detail?
- [ ] Cek HTTP request - apakah Authorization header ada?
- [ ] Cek Network Tab - response code berapa? (500, 409, 404?)

### ✅ Di Backend (Node.js):
- [ ] Cek error log - apa exact error message?
- [ ] Run: `SELECT * FROM barang WHERE toko_id = [ID];` - ada data?
- [ ] Run: `SELECT * FROM order_detail WHERE barang_id = [ID];` - ada data?
- [ ] Test endpoint manual dengan Postman: `DELETE http://localhost:3000/api/toko/1`
- [ ] Cek apakah foreign key constraint aktif di database?

---

## 🎯 NEXT STEPS

1. **Jika error adalah Foreign Key Constraint (409)**
   → Gunakan Cascade Delete atau Delete referensi dulu

2. **Jika error adalah SQL Syntax (500)**
   → Periksa query syntax di backend

3. **Jika error adalah Authorization (401)**
   → Cek apakah middleware auth diterapkan

4. **Jika error adalah 404**
   → Cek apakah route terdaftar di backend

---

## 📝 TESTING PROCEDURE

```bash
# 1. Test dengan Postman (Backend Testing)
DELETE http://localhost:3000/api/toko/1
Authorization: Bearer [TOKEN]

# Expected responses:
# - 200: Success
# - 404: Not Found
# - 409: Conflict (ada referensi)
# - 500: Server Error

# 2. Check database
SELECT * FROM barang WHERE toko_id = 1;
SELECT * FROM order_detail WHERE barang_id = 1;

# 3. Check logs
tail -f /path/to/backend/logs
```

---

## 🔗 Referensi Kode

**Android Files Terlibat:**
- `TokoViewModel.kt` - Delete logic
- `BarangViewModel.kt` - Delete logic
- `TokoListScreen.kt` - UI callback
- `BarangScreen.kt` - UI callback

**Backend Files Harus Dicek:**
- `TokoController.js` atau `TokoRoutes.js`
- `BarangController.js` atau `BarangRoutes.js`

---


