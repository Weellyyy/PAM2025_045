# 🔧 FIX BACKEND: Menyimpan Gambar dengan Path yang Benar

## Problem
Android sudah fix untuk handle `/public`, tetapi backend HARUS menyimpan file gambar di folder yang tepat dengan path yang benar.

---

## ✅ Solution: Update barang.controller.js

**File:** `backend/controllers/barang.controller.js`

```javascript
const Barang = require('../model/barang');
const fs = require('fs');
const path = require('path');

const barangController = {
    
    createBarang: (req, res) => {
        const { nama_barang, stok, harga, gambar_base64 } = req.body;

        if (!nama_barang || stok === undefined || !harga) {
            return res.status(400).json({ 
                message: 'nama_barang, stok, dan harga harus diisi' 
            });
        }

        let gambarUrl = null;

        // ✅ Jika ada gambar_base64, save ke public folder
        if (gambar_base64) {
            try {
                // Buat folder jika belum ada
                const uploadDir = path.join(__dirname, '../public/barang');
                if (!fs.existsSync(uploadDir)) {
                    fs.mkdirSync(uploadDir, { recursive: true });
                }

                // Generate nama file unique
                const fileName = `barang_${Date.now()}_${Math.random().toString(36).substr(2, 9)}.jpg`;
                const filePath = path.join(uploadDir, fileName);

                // Decode base64 dan save ke file
                const buffer = Buffer.from(gambar_base64, 'base64');
                fs.writeFileSync(filePath, buffer);

                // ✅ PENTING: Set path ke /public/barang/xxx.jpg
                gambarUrl = `/public/barang/${fileName}`;
                
                console.log(`[BARANG] Gambar disimpan: ${gambarUrl}`);

            } catch (error) {
                console.error('[BARANG] Error save gambar:', error.message);
                return res.status(500).json({ 
                    message: 'Gagal menyimpan gambar',
                    error: error.message 
                });
            }
        }

        // Insert ke database
        Barang.create(
            {
                nama_barang,
                stok: parseInt(stok),
                harga: parseFloat(harga),
                gambar_url: gambarUrl  // ← Simpan path /public/...
            },
            (err, result) => {
                if (err) {
                    console.error('[BARANG] Error create:', err.message);
                    return res.status(500).json({ 
                        message: 'Gagal membuat barang',
                        error: err.message 
                    });
                }

                res.status(201).json({
                    message: 'Barang berhasil dibuat',
                    barang_id: result.insertId,
                    gambar_url: gambarUrl
                });
            }
        );
    },

    getAllBarang: (req, res) => {
        Barang.getAll((err, results) => {
            if (err) {
                console.error('[BARANG] Error getAll:', err.message);
                return res.status(500).json({ message: 'Server error' });
            }
            
            console.log(`[BARANG] Fetched ${results.length} items`);
            res.json(results);
        });
    },

    getBarangById: (req, res) => {
        Barang.getById(req.params.id, (err, results) => {
            if (err) return res.status(500).json({ message: 'Server error' });
            if (results.length === 0) return res.status(404).json({ message: 'Barang tidak ditemukan' });
            res.json(results[0]);
        });
    },

    updateBarang: (req, res) => {
        const { nama_barang, stok, harga, gambar_base64 } = req.body;
        const barangId = req.params.id;

        Barang.getById(barangId, (err, results) => {
            if (err || results.length === 0) {
                return res.status(404).json({ message: 'Barang tidak ditemukan' });
            }

            const oldBarang = results[0];
            let gambarUrl = oldBarang.gambar_url;

            // ✅ Jika ada gambar_base64 baru, replace
            if (gambar_base64) {
                try {
                    // Hapus file lama
                    if (oldBarang.gambar_url) {
                        const oldFilePath = path.join(__dirname, '..', oldBarang.gambar_url);
                        if (fs.existsSync(oldFilePath)) {
                            fs.unlinkSync(oldFilePath);
                            console.log(`[BARANG] File lama dihapus: ${oldFilePath}`);
                        }
                    }

                    // Save file baru
                    const uploadDir = path.join(__dirname, '../public/barang');
                    if (!fs.existsSync(uploadDir)) {
                        fs.mkdirSync(uploadDir, { recursive: true });
                    }

                    const fileName = `barang_${Date.now()}_${Math.random().toString(36).substr(2, 9)}.jpg`;
                    const filePath = path.join(uploadDir, fileName);
                    const buffer = Buffer.from(gambar_base64, 'base64');
                    fs.writeFileSync(filePath, buffer);

                    gambarUrl = `/public/barang/${fileName}`;
                    console.log(`[BARANG] Gambar baru disimpan: ${gambarUrl}`);

                } catch (error) {
                    console.error('[BARANG] Error update gambar:', error.message);
                    return res.status(500).json({ 
                        message: 'Gagal menyimpan gambar',
                        error: error.message 
                    });
                }
            }

            Barang.update(
                barangId,
                {
                    nama_barang,
                    stok: parseInt(stok),
                    harga: parseFloat(harga),
                    gambar_url: gambarUrl
                },
                (err) => {
                    if (err) {
                        return res.status(500).json({ message: 'Gagal mengupdate barang' });
                    }
                    res.json({ message: 'Barang berhasil diupdate', gambar_url: gambarUrl });
                }
            );
        });
    },

    deleteBarang: (req, res) => {
        const barangId = req.params.id;

        Barang.getById(barangId, (err, results) => {
            if (err || results.length === 0) {
                return res.status(404).json({ message: 'Barang tidak ditemukan' });
            }

            const barang = results[0];

            // Hapus file gambar jika ada
            if (barang.gambar_url) {
                const filePath = path.join(__dirname, '..', barang.gambar_url);
                if (fs.existsSync(filePath)) {
                    fs.unlinkSync(filePath);
                    console.log(`[BARANG] File dihapus: ${filePath}`);
                }
            }

            Barang.delete(barangId, (err) => {
                if (err) {
                    return res.status(500).json({ message: 'Gagal menghapus barang' });
                }
                res.json({ message: 'Barang berhasil dihapus' });
            });
        });
    }
};

module.exports = barangController;
```

---

## ✅ Checklist Untuk Backend

1. **Update `barang.controller.js`** ✓ (Copy code di atas)

2. **Pastikan folder ada:**
   ```bash
   mkdir -p public/barang
   ```

3. **Pastikan `app.js` punya:**
   ```javascript
   app.use('/public', express.static(path.join(__dirname, 'public')));
   app.use(express.json({ limit: '50mb' }));
   ```

4. **Restart server:**
   ```bash
   npm start
   ```

---

## 🧪 Test

### Test 1: Cek Response API
```bash
curl http://localhost:3000/api/barang
```

Harusnya muncul:
```json
[
  {
    "barang_id": 1,
    "nama_barang": "Mie Instan",
    "stok": 100,
    "harga": "3500.00",
    "gambar_url": "/public/barang/barang_1735927200_abc.jpg"
  }
]
```

### Test 2: Akses gambar di browser
```
http://localhost:3000/public/barang/barang_1735927200_abc.jpg
```

Jika gambar muncul = Backend OK ✅

### Test 3: Run Android
```bash
./gradlew clean
./gradlew build
./gradlew installDebug
```

Cek logcat:
```bash
adb logcat | grep "BarangCard"
```

Harusnya muncul:
```
BarangCard: Raw gambarUrl: /public/barang/barang_1735927200_abc.jpg
BarangCard: Final imageUrl: http://10.0.2.2:3000/public/barang/barang_1735927200_abc.jpg
BarangCard: ✅ Image loaded successfully: http://10.0.2.2:3000/public/barang/barang_1735927200_abc.jpg
```

---

## ✅ Expected Result

Setelah semua ini di-update:

1. Upload barang dengan foto
2. Foto tersimpan di `backend/public/barang/`
3. Database punya path `/public/barang/xxx.jpg`
4. Android load dari `http://10.0.2.2:3000/public/barang/xxx.jpg`
5. **Gambar muncul di BarangCard** ✅

---

**Prioritas:** Update backend controller dulu, baru test Android!


