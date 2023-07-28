
# Hazelcast Caching

Hazelcast, Java tabanlı özgür bir bellek içi veri ızgarasıdır. Bir Hazelcast ızgarasında, veriler bir bilgisayar kümesinin düğümleri arasında eşit olarak dağıtılır, böylece işleme ve kullanılabilir depolama yatay ölçeklenebilir. Yedeklemeler, herhangi bir düğümün başarısızlığına karşı korumak için düğümler arasında dağıtılır. Hazelcast, sık kullanılan verilere bellek içi erişim ve öngörülebilir ölçeklendirmesini sağlar. Bu teknikler veritabanlarındaki sorgu yükünü azaltır.

Hazelcast verileri hafızada bir map şeklinde tutuyor. Bu verileri Hazelcast aracılığı sadece yönetmekle kalmayıp, verileri kitlemei kuyruğa alma gibi işlemleri yapabiliyoruz. Veriler ram'de tutulurken bir o kadarı da backup şeklinde yine bellekler arasında paylaştırılıp veri kaybını en aza indirmeyi anaçlıyor.


# Kullanımı

## Maven'a dependency ekliyoruz

```http
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast</artifactId>
    <version>5.2.3</version>
</dependency>
```

## Caching'i aktif ediyoruz.
**@EnableCaching**
```http
@SpringBootApplication
@EnableCaching
public class HazelcastSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastSpringBootApplication.class, args);
    }

}
```
@EnableCaching anotasyonu ile projemizde caching işlemini aktif ediyoruz.

## CacheConfig ile cache ismini belirliyoruz.
**@CacheConfig(cacheNames = "students")**
```http
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
@CacheConfig(cacheNames = "students")
public class StudentController 
```

**@CacheConfig** Spring Framework'de kullanılan bir anotasyondur ve belirli bir sınıftaki tüm **@Cacheable**, **@CachePut**, **@CacheEvict** veya **@Caching** gibi diğer önbellekleme anotasyonlarının paylaşacakları önbellek yapılandırmasını tanımlar.

## Liste dönen verinin cachelenmesi .
**@Cacheable(value = "student_list")**
```http
  @GetMapping
  @Cacheable(value = "student_list")
  public List<Student> getAll(){
      return service.getAll();
  }
```

**@Cacheable** anotasyonu ile **getAll()** metodunu önbelleğe aldık. **value = "student_list"** ifadesi, bu önbelleğin bir adı olduğunu belirtir. Aynı isimle önbelleğe alınan sonuçlar, diğer çağrılarda tekrar kullanılabilir.

İlk çağrıda **getAll()** metodunun sonucu alınır ve önbelleğe alınır. Sonraki çağrılarda aynı metodun çağrılması durumunda, sonuç önbellekten alınır ve tekrar **service.getAll()** çağrısı yapılmaz. Bu şekilde her seferinde veritabanında sorgu atılmamış olur.

## ID ile dönen verinin cachelenmesi .
**@Cacheable(key = "#id")**
```http
  @GetMapping("/{id}")
  @Cacheable(key = "#id")
  public Student getById(@PathVariable UUID id){
      return service.getById(id);
  }
```

**@Cacheable** anotasyonu sayesinde, metodu çalıştırdığımızda bu metoda geçirilen **id** parametresine bağlı olarak elde edilen sonucu önbelleğe alır. Sonraki çağrılarda aynı **id** ile yapılan isteklerde, önbelleğe alınmış sonuçlar hızlı bir şekilde döndürülür.

**key = "#id"** ifadesi, önbelleğe alınan sonuçların hangi anahtar altında saklanacağını belirtir. **#id**, metoduna geçirilen *id* parametresini temsil eder. Yani, her farklı *id* değeri için ayrı bir önbellek girdisi oluşturulur.

İlk çağrıda **getById** metoduna *id* değeri *a74d44b5-f87e-4c1a-82f7-58cd1d8d822e* ile bir istek yapıldığında sonuç elde edilir ve önbelleğe *a74d44b5-f87e-4c1a-82f7-58cd1d8d822e* anahtarı altında saklanır. Sonraki çağrılarda aynı *id* değeri olan istekler yapıldığında sorgu tekrardan çalıştırılmadan sonuç doğrudan önbellekten verilir.

## Ekleme işleminde sonra cachelerin temizlenmesi.
**@CacheEvict(value = "student_list", allEntries = true)**
```http
  @PostMapping
  @CacheEvict(value = "student_list", allEntries = true)
  public Student add(@RequestBody Student student){
      return service.add(student);
  }
```

**@CacheEvict** Spring Framework'de kullanılan bir anotasyondur ve belirli bir metodun çalıştığında, önbellekteki belirli bir öğenin (veya önbelleklerin) silinmesini sağlar.

**@CacheEvict** anotasyonu, **add** metodunun çağrılında **student_list** adlı önbelleği temizler. Bu sayede tekrardan **getAll** metodu çalıştığında sorgu atılacak ve veri güncellenmiş olacak. **@CacheEvict** atonasyonunda, **value** parametresi ile temizlenecek olan önbellek adı belirtilir. **allEntries = true** yaparak da bu parametredeki tüm verilerin temizlenmesini sağladık.

## Güncelleme işleminde sonra cachelerin güncellenmesi ve temizlenmesi.
**@CachePut(key = "#id")**
```http
  @PutMapping("/{id}")
  @CachePut(key = "#id")
  @CacheEvict(value = "student_list",allEntries = true)
  public Student update(@PathVariable UUID id,@RequestBody Student student ){
      return service.update(id,student);
  }
```

**@CachePut** Spring Framework'te kullanılan bir anotasyondur ve belirli bir metodun sonucunu önbelleğe alırken aynı zamanda gerçek metodu da çağırmayı sağlar. Yani, verilen bir anahtara bağlı olarak elde edilen sonucu önbelleğe günceller ve aynı sonucu da metotun gerçekten çalışmasını sağlar.

**@CachePut** anotasyonu, **update** metodunun sonucunu **#id** ifadesinde belirtilen anahtarla önbelleğe alır. **#id**, metoda geçirilen *id* parametresini temsil eder ve bu şekilde önbellekte veriyi bu *id* ile ilişkilendirir.

Aynı zamanda, **@CacheEvict** anotasyonu ile **student_list** önbelleğini de temizlemekteyiz. Bu sayede hem **id** ile bağlantılı cache hem de **student_list** ile bağlantılı cache'ler güncellenmiş oldu.

Bu kullanım, **update** metodunun sonucu güncellendikçe, önbellekteki veriyi güncel tutmak için kullanılır. Böylece metodu her çağırdığımızda, güncel veriyi önbellekten alabilir ve aynı zamanda veritabanı veya diğer yavaş kaynaklar üzerindeki iş yükünü azaltarak performasnı da arttırmış oluruz.