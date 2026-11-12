# ATLAS AI — Android başlangıç projesi

Bu, Samsung Galaxy S25 Ultra'da denenmek üzere hazırlanmış **ilk prototip kaynak projesidir**.
Henüz tam Jarvis değildir: düğmeye basınca Türkçe konuşmayı tanır, birkaç temel yerel komuta cevap verir ve telefonun TTS motoruyla sesli yanıt verir.

## Telefonda APK oluşturma (bilgisayar gerekmez)

1. GitHub hesabı aç veya mevcut hesabına giriş yap.
2. Bu ZIP'i telefonda çıkar.
3. GitHub'da yeni, boş bir depo oluştur (ör. `atlas-ai`).
4. ZIP içindeki dosyaları deponun ana dizinine yükle. `.github/workflows/build-apk.yml` dosyasının da yüklendiğini kontrol et.
5. Depoda **Actions** sekmesini aç. `Build ATLAS APK` iş akışını seçip **Run workflow** düğmesine bas.
6. İşlem başarılı olunca çalışmanın altındaki **Artifacts** bölümünden `ATLAS-debug-apk` dosyasını indir.
7. ZIP içinden `app-debug.apk` dosyasını çıkarıp aç. Android isterse yalnızca bu kurulum için tarayıcıya/dosya yöneticisine “bilinmeyen uygulama yükleme” izni ver.

## Telefonda ses ayarı

Ayarlar > Genel yönetim > Metin okuma (Text-to-speech) bölümünden Türkçe ses verisi varsa indir. Sesin kalitesi ve erkek/kadın seçimi telefondaki TTS motoruna bağlıdır.

## Şeffaf sınırlar

- Telefon tamamen kapalıyken hiçbir uygulama dinleyemez.
- Bu prototip sürekli “Hey Atlas” dinlemez; uygulama açıkken konuş düğmesine basınca dinler.
- Android'in arka plan mikrofon kısıtlamaları nedeniyle ekran kapalı sürekli dinleme ayrı bir foreground-service ve görünür bildirim tasarımı gerektirir.
- Gerçek çevrimiçi LLM bağlantısı bu ilk prototipte yoktur. Güvenli bir sunucu/API bağlantısı sonraki aşamada eklenmelidir; API anahtarını APK içine koymak güvenli değildir.
- İnternetsiz konuşma tanıma, cihazda Türkçe çevrimdışı konuşma paketi bulunmasına bağlıdır.
