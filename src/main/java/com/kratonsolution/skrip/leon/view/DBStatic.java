/**
 * 
 */
package com.kratonsolution.skrip.leon.view;

/**
 * @author agung
 *
 */
public class DBStatic {

	public static final String GANGGUAN[] = new String[] {
			"TLP mati total",
		    "Kabel dropcore rusak",
		    "Alamat dari server otentik tidak sesuai",
		    "Konfigurasi setting hilang",
		    "Kabel UTP LAN/Connector RJ 45 rusak"
	};
	
	public static final String SOLUSI[] = new String[] {
			
		     "Apabila telepon mati tidak ada nada adalah cek dengan test phone kemungkinan pesawat telepon pelanggan yang rusak",
		     "jika jaringan telepon, rekonek kabel UTP telepon",
		     "Cek pada splitter pastika RJ 11 yang masuk pada phone dan modem tidak terbalik",
		     "Apabila tlp di parallel cek pada sambungan rosette pastikan tidak lembab air",
		     "Pertama kali cabut RJ 11 pada pesawat telepon, kemudian pasang kembali",
		     "Cek ukuran/redaman kabel dengan menggunakan OPM meter (ukuran baik tidak >25dBm), lalu lakukan rekoneksi sambungan/pergantian dropcore.",
		     "Setting create pada modem hilang, lakukan create ulang, masuk pada putty, masukkan script create setting konfigurasi dan masukkan data nomor telepon, nomor internet, dan SN (Serial Number) modem",
		     "Dicoba dengan reboot STB (set top box) dengan cara: pencet tombol SET pada remote useetv> pilih konfigurasi> isi password dengan 6321> tingkat lanjut> reboot.",
		     "Pastikan kabel UTP dari STB menancap di LAN Port 4 modem, agar mendapatkan alamat dari server yaitu ip 10 .x.x.x",
		     "Jika kualitas gambar tv putus-putus, tekan tombol info pada remote control> panah kanan [volume +] > tampil signal power & quality standard>70%. Atau dilakukan create ulang konfigurasi.",
		     "Jika gangguan error 1901. Disebabkan kabel jaringan tidak tersambung. Periksa koneksi fisik dari kabel jaringan.",
		     "Cek koneksi kabel UTP & RJ 45 dari modem ke arah STB, coba di-reconnect ulang.",
		     "Apabila kabel UTP & RJ 45 sudah tidak berfungsi dapat dilakukan penggantian baru.",
		     "Ganti kabel patchcord baru, melakukan penyambungan ulang dengan kabel dropcore.",
		     "Dilakukan penggantian fast connector baru, kemudian lakukan penyambungan ulang",
		     "Dilakukan pengecekan dengan adaptor dan modem test, apabila tidak berfungsi dapat diganti dengan adaptor atau modem ONT baru.",
		     "Periksa koneksi fisik dari kabel jaringan. Cek koneksi kabel UTP & RJ 45 dari modem ke arah STB, coba di-reconnect ulang. Apabila kabel UTP & RJ 45 sudah tidak berfungsi dapat dilakukan penggantian baru.",
		     "Cek kelayakan kabel UTP, connector RJ 45 dari ONT ke STB. Cek dengan modem test, apabila STB rusak dapat dilakukan penggantian modem baru.",
		     "menghubungkan terminal telkom yang terdapat di atas tiang (ODP) ke pelanggan selain bisa di lihat secara fisik putusnya juga bisa di lihat pada lampu indikator modem (lampu los) yang menyala merah.",
		     "Restart Modem dengan cara mematikan Modem (Power OFF) selama 3 menit kemudian modem di nyalakan kembali"
	};
}
