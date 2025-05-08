package com.example.breakfreeBE.addiction.service;

import com.example.breakfreeBE.addiction.dto.AddictionDTO;
import com.example.breakfreeBE.addiction.entity.Addiction;
import com.example.breakfreeBE.addiction.entity.AddictionId;
import com.example.breakfreeBE.addiction.repository.AddictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddictionService {

    @Autowired
    private AddictionRepository addictionRepository;

    // Konversi entity Addiction ke DTO AddictionDTO
    private AddictionDTO convertToDTO(Addiction addiction) {
        AddictionDTO dto = new AddictionDTO();
        dto.setUserId(addiction.getUserId());
        dto.setAddictionId(addiction.getAddictionId());
        dto.setSaver(addiction.getSaver());
        dto.setStreaks(addiction.getStreaks());
        dto.setMotivation(addiction.getMotivation());
        dto.setStartDate(addiction.getStartDate());

        if (addiction.getAddictionData() != null) {
            dto.setAddictionName(addiction.getAddictionData().getAddictionName()); // misal field `name` di AddictionData
        }

        return dto;
    }

    public static class StreakFunFact {
        public String fact;
        public String imageUrl;
        public String bgColor;

        public StreakFunFact(String fact, String imageUrl, String bgColor) {
            this.fact = fact;
            this.imageUrl = imageUrl;
            this.bgColor = bgColor;
        }
    }

    public static StreakFunFact getStreakFunFact(long streak) {
        if (streak >= 1095) {
            return new StreakFunFact("Dalam 3 tahun, Anda menghirup sekitar 31 juta kali, namun berapa kali Anda benar-benar menyadarinya?", "", "#3B3B98");
        } else if (streak >= 730) {
            return new StreakFunFact("Dalam 2 tahun, Anda menghabiskan 730 jam di kamar mandi, waktu yang cukup untuk menguasai instrumen musik!", "", "#4B7BE5");
        } else if (streak >= 365) {
            return new StreakFunFact("Dalam 1 tahun, Anda menghabiskan 1.460 jam (60 hari penuh) hanya untuk scrolling media sosial!", "", "#FF6B6B");
        } else if (streak >= 90) {
            return new StreakFunFact("Dalam 3 bulan, Anda melihat ponsel rata-rata 14.000 kali, bayangkan jika setiap pandangan adalah ide baru!", "", "#FFA502");
        } else if (streak >= 60) {
            return new StreakFunFact("Dalam 2 bulan, rambut Anda tumbuh sekitar 2,5 cm, bukti diam-diam bahwa perubahan selalu terjadi!", "", "#2ED573");
        } else if (streak >= 30) {
            return new StreakFunFact("Dalam 1 bulan, Anda menghabiskan rata-rata 720 jam, namun hanya 26 jam yang benar-benar produktif!", "", "#1E90FF");
        } else if (streak >= 14) {
            return new StreakFunFact("Dalam 2 minggu, sel kulit Anda sepenuhnya berganti yang baru, memberi Anda kesempatan memulai dari awal!", "", "#70A1FF");
        } else if (streak >= 7) {
            return new StreakFunFact("Dalam 1 minggu, Anda menelan sekitar 7 liter air liur, cukup untuk mengisi botol minuman olahraga besar!", "", "#A29BFE");
        } else if (streak >= 5) {
            return new StreakFunFact("Dalam 5 hari, Anda menghabiskan rata-rata 5 jam mencari barang yang salah tempat, waktu yang bisa digunakan untuk belajar hal baru!", "", "#E17055");
        } else if (streak >= 3) {
            return new StreakFunFact("Dalam 3 hari, jantung Anda berdetak sekitar 312.000 kali, bekerja tanpa henti untuk Anda!", "", "#F8C291");
        } else if (streak >= 1) {
            return new StreakFunFact("Dalam 1 hari, jantung Anda memompa darah sejauh 19.000 km, setara dengan setengah keliling bumi!", "", "#00B894");
        } else {
            return new StreakFunFact("Hari ini saja, Anda akan berkedip sekitar 15.000 kali tanpa menyadarinya!", "", "#CAD3C8");
        }
    }

    public StreakFunFact getSavingFunFact(String userId) {
        List<AddictionDTO> addictions = getAddictionsByUser(userId);
        if (addictions.isEmpty()) {
            // Tidak ada data, kembalikan null
            return null;
        }
        long totalSaved = 0;

        for (AddictionDTO addiction : addictions) {
            if (addiction.getStartDate() != null && addiction.getSaver() != null) {
                long daysPassed = ChronoUnit.DAYS.between(addiction.getStartDate(), LocalDate.now());
                totalSaved += daysPassed * addiction.getSaver();
            }
        }

        if (totalSaved >= 100_000_000) {
            return new StreakFunFact(
                    "Rp 100 juta yang diinvestasikan dengan bijak bisa menghasilkan kebebasan finansial yang dinikmati hanya oleh 0,1% populasi dunia!",
                    "", "#1A1A1A"
            );
        } else if (totalSaved >= 50_000_000) {
            return new StreakFunFact(
                    "Rp 50 juta cukup untuk membeli 1.000 pohon yang akan menyerap 500 ton karbon selama masa hidupnya!",
                    "", "#228B22"
            );
        } else if (totalSaved >= 20_000_000) {
            return new StreakFunFact(
                    "Rp 20 juta cukup untuk membeli peralatan elektronik canggih untuk mempermudah hidup anda",
                    "", "#4682B4"
            );
        } else if (totalSaved >= 10_000_000) {
            return new StreakFunFact(
                    "Rp 10 juta yang digunakan untuk memulai bisnis kecil memiliki potensi menciptakan aliran pendapatan yang bisa mendukung keluargamu selama beberapa generasi!",
                    "", "#FF8C00"
            );
        } else if (totalSaved >= 5_000_000) {
            return new StreakFunFact(
                    "Dengan Rp 5 juta, kamu bisa memulai investasi yang dalam 30 tahun bisa tumbuh menjadi dana pensiun Rp 100 juta!",
                    "", "#FFD700"
            );
        } else if (totalSaved >= 3_000_000) {
            return new StreakFunFact(
                    "Rp 3 juta yang digunakan untuk liburan menciptakan kenangan yang akan diingat 10.000 kali lebih lama daripada barang seharga sama!",
                    "", "#FF69B4"
            );
        } else if (totalSaved >= 1_000_000) {
            return new StreakFunFact(
                    "Rp 1 juta yang diinvestasikan dalam kursus online dapat memberikanmu keterampilan yang meningkatkan penghasilanmu hingga 300% dalam 5 tahun ke depan!",
                    "", "#6A5ACD"
            );
        } else if (totalSaved >= 500_000) {
            return new StreakFunFact(
                    "Dengan Rp 500.000, kamu bisa membeli sepatu lari yang akan bertahan 1.000 km, setara dengan perjalanan Jakarta-Surabaya pulang pergi!",
                    "", "#DC143C"
            );
        } else if (totalSaved >= 100_000) {
            return new StreakFunFact(
                    "Rp 100.000 cukup untuk membeli bahan makanan yang menghasilkan 20 kali lipat energi dibanding fast food seharga sama!",
                    "", "#32CD32"
            );
        } else if (totalSaved >= 50_000) {
            return new StreakFunFact(
                    "Rp 50.000 bisa membeli buku bekas yang berisi pengetahuan bernilai jutaan rupiah untuk masa depanmu!",
                    "", "#8B4513"
            );
        } else if (totalSaved >= 10_000) {
            return new StreakFunFact(
                    "Dengan Rp 10.000, kamu bisa membeli sebuah buku catatan yang mampu menampung lebih dari 5.000 ide brilian yang mungkin mengubah hidupmu!",
                    "", "#00CED1"
            );
        } else {
            return new StreakFunFact(
                    "Rp 0 adalah jumlah yang sama yang dimiliki Jeff Bezos dan Bill Gates saat mereka memulai bisnis pertama mereka!",
                    "", "#A9A9A9"
            );
        }
    }

    // Konversi DTO ke entity Addiction
    private Addiction convertToEntity(AddictionDTO dto) {
        Addiction addiction = new Addiction();
        addiction.setUserId(dto.getUserId());
        addiction.setAddictionId(dto.getAddictionId());
        addiction.setSaver(dto.getSaver());
        addiction.setStreaks(dto.getStreaks());
        addiction.setMotivation(dto.getMotivation());
        addiction.setStartDate(dto.getStartDate());
        return addiction;
    }

    // Mendapatkan Addiction berdasarkan UserId dan mengembalikan dalam bentuk AddictionDTO
    public List<AddictionDTO> getAddictionsByUser(String userId) {
        List<Addiction> addictions = addictionRepository.findAddictionsByUserId(userId);
        return addictions.stream()
                .map(this::convertToDTO) // convert Addiction entity ke AddictionDTO
                .collect(Collectors.toList());
    }

    // Menyimpan Addiction baru dan mengembalikan AddictionDTO
    public Optional<AddictionDTO> saveAddiction(AddictionDTO dto) {
        Addiction addiction = convertToEntity(dto); // convert DTO ke Addiction entity
        if (addictionRepository.existsById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))) {
            return Optional.empty(); // Addiction sudah ada
        }
        Addiction savedAddiction = addictionRepository.save(addiction);
        return Optional.of(convertToDTO(savedAddiction)); // Mengembalikan AddictionDTO
    }

    // Mengupdate Addiction dan mengembalikan AddictionDTO
    public Optional<AddictionDTO> updateAddiction(AddictionDTO dto) {
        Addiction addiction = convertToEntity(dto); // convert DTO ke Addiction entity
        return addictionRepository.findById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))
                .map(existingAddiction -> {
                    if (addiction.getSaver() != null) {
                        existingAddiction.setSaver(addiction.getSaver());
                    }
                    if (addiction.getMotivation() != null) {
                        existingAddiction.setMotivation(addiction.getMotivation());
                    }
                    Addiction updatedAddiction = addictionRepository.save(existingAddiction);
                    return convertToDTO(updatedAddiction); // Mengembalikan AddictionDTO
                });
    }

    // Mereset Addiction dan mengembalikan AddictionDTO
    public Optional<AddictionDTO> resetAddiction(AddictionDTO dto) {
        Addiction addiction = convertToEntity(dto); // convert DTO ke Addiction entity
        return addictionRepository.findById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))
                .map(existingAddiction -> {
                    LocalDate oldStartDate = existingAddiction.getStartDate();
                    LocalDate newStartDate = LocalDate.now();
                    long duration = ChronoUnit.DAYS.between(oldStartDate, newStartDate);
                    existingAddiction.setStreaks(duration);
                    existingAddiction.setStartDate(newStartDate);
                    Addiction resetAddiction = addictionRepository.save(existingAddiction);
                    return convertToDTO(resetAddiction); // Mengembalikan AddictionDTO
                });
    }

    // Menghapus Addiction dan mengembalikan status sukses
    public boolean deleteAddiction(AddictionDTO dto) {
        Addiction addiction = convertToEntity(dto); // convert DTO ke Addiction entity
        AddictionId addictionId = new AddictionId(addiction.getUserId(), addiction.getAddictionId());
        if (!addictionRepository.existsById(addictionId)) {
            return false;
        }
        addictionRepository.deleteById(addictionId);
        return true;
    }
}