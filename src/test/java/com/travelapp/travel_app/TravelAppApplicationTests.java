package com.travelapp.travel_app;

import com.travelapp.travel_app.model.Atractie;
import com.travelapp.travel_app.model.Oras;
import com.travelapp.travel_app.model.Tara;
import com.travelapp.travel_app.repository.AtractieRepository;
import com.travelapp.travel_app.repository.OrasRepository;
import com.travelapp.travel_app.repository.TaraRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.travelapp.travel_app.model.Status.FINALIZATA;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TravelAppApplicationTests {

	@Autowired
	private OrasRepository orasRepository;

	@Autowired
	private TaraRepository taraRepository;

	@Autowired
	private AtractieRepository atractieRepository;

	// Teste pentru operațiile CRUD
	// Test pentru crearea și salvarea unui oraș
	@Test
	public void testCreateAndSaveCity() {
		// Creare oraș
		Oras oras = new Oras();
		oras.setNume("Bucuresti");
		oras.setDescriere("Capitala României.");

		// Salvare oraș
		Oras savedOras = orasRepository.save(oras);

		// Verificare salvare
		assertNotNull(savedOras.getId());
		assertEquals("Bucuresti", savedOras.getNume());
	}

	// Test pentru găsirea unui oraș după ID
	@Test
	public void testFindCityById() {
		// Creare și salvare oraș
		Oras oras = new Oras();
		oras.setNume("Cluj Napoca");
		oras.setDescriere("Oraș universitar.");
		Oras savedOras = orasRepository.save(oras);

		// Căutare după ID
		Optional<Oras> foundOras = orasRepository.findById(savedOras.getId());

		// Verificare
		assertTrue(foundOras.isPresent());
		assertEquals("Cluj Napoca", foundOras.get().getNume());
	}

	// Test pentru ștergerea unui oraș fără atracții
	@Test
	public void testDeleteCityWithoutAttractions() {
		// Creare și salvare oraș
		Oras oras = new Oras();
		oras.setNume("Test City");
		oras.setDescriere("Descriere de test.");
		orasRepository.save(oras);

		// Ștergere oraș
		orasRepository.delete(oras);

		// Verificare dacă orașul a fost șters
		Optional<Oras> deletedOras = orasRepository.findById(oras.getId());
		assertTrue(deletedOras.isEmpty());
	}

	@Test
	public void testListAllCities() {
		// Creare și salvare orașe
		Oras oras1 = new Oras();
		oras1.setNume("Brasov");
		oras1.setDescriere("Oraș montan.");
		orasRepository.save(oras1);

		Oras oras2 = new Oras();
		oras2.setNume("Constanta");
		oras2.setDescriere("Oraș portuar.");
		orasRepository.save(oras2);

		// Listare toate orașele
		List<Oras> orasList = orasRepository.findAll();

		// Verificare
		assertFalse(orasList.isEmpty());
		assertTrue(orasList.stream().anyMatch(o -> o.getNume().equals("Brasov")));
		assertTrue(orasList.stream().anyMatch(o -> o.getNume().equals("Constanta")));
	}

	@Test
	public void testCreateAndSaveCountry() {
		// Creare țară
		Tara tara = new Tara();
		tara.setNume("Romania");

		// Salvare țară
		Tara savedTara = taraRepository.save(tara);

		// Verificare salvare
		assertNotNull(savedTara.getId());
		assertEquals("Romania", savedTara.getNume());
	}

	@Test
	public void testDeleteCountry() {
		// Creare și salvare țară
		Tara tara = new Tara();
		tara.setNume("Romania");
		taraRepository.save(tara);

		// Ștergere țară
		taraRepository.delete(tara);

		// Verificare ștergere
		Optional<Tara> deletedTara = taraRepository.findById(tara.getId());
		assertTrue(deletedTara.isEmpty());
	}

	@Test
	public void testListAllCountries() {
		// Creare și salvare țări
		Tara tara1 = new Tara();
		tara1.setNume("Romania");
		taraRepository.save(tara1);

		Tara tara2 = new Tara();
		tara2.setNume("Bulgaria");
		taraRepository.save(tara2);

		// Listare țări
		List<Tara> taraList = taraRepository.findAll();

		// Verificare
		assertFalse(taraList.isEmpty());
		assertTrue(taraList.stream().anyMatch(t -> t.getNume().equals("Romania")));
		assertTrue(taraList.stream().anyMatch(t -> t.getNume().equals("Bulgaria")));
	}

	@Test
	public void testCreateAndSaveAttraction() {
		// Creare atracție
		Atractie atractie = new Atractie();
		atractie.setNume("Castelul Bran");
		atractie.setDescriere("Un castel istoric situat în România.");
		atractie.setStatus(FINALIZATA);

		// Salvare atracție
		Atractie savedAtractie = atractieRepository.save(atractie);

		// Verificare salvare
		assertNotNull(savedAtractie.getId());
		assertEquals("Castelul Bran", savedAtractie.getNume());
		assertEquals(FINALIZATA, savedAtractie.getStatus());
	}

	@Test
	public void testDeleteAttraction() {
		// Creare și salvare atracție
		Atractie atractie = new Atractie();
		atractie.setNume("Delta Dunarii");
		atractie.setDescriere("Rezervație naturală din România.");
		atractie.setStatus(FINALIZATA);
		atractieRepository.save(atractie);

		// Ștergere atracție
		atractieRepository.delete(atractie);

		// Verificare ștergere
		Optional<Atractie> deletedAtractie = atractieRepository.findById(atractie.getId());
		assertTrue(deletedAtractie.isEmpty());
	}

	@Test
	public void testListAllAttractions() {
		// Creare și salvare atracții
		Atractie atractie1 = new Atractie();
		atractie1.setNume("Castelul Peles");
		atractie1.setDescriere("Castelul regilor României.");
		atractie1.setStatus(FINALIZATA);
		atractieRepository.save(atractie1);

		Atractie atractie2 = new Atractie();
		atractie2.setNume("Transfagarasan");
		atractie2.setDescriere("Un drum spectaculos prin munți.");
		atractie2.setStatus(FINALIZATA);
		atractieRepository.save(atractie2);

		// Listare atracții
		List<Atractie> atractieList = atractieRepository.findAll();

		// Verificare
		assertFalse(atractieList.isEmpty());
		assertTrue(atractieList.stream().anyMatch(a -> a.getNume().equals("Castelul Peles")));
		assertTrue(atractieList.stream().anyMatch(a -> a.getNume().equals("Transfagarasan")));
	}

	@Test
	public void testAssignCountryToCity() {
		// Creare țară
		Tara tara = new Tara();
		tara.setNume("Romania");
		tara = taraRepository.save(tara);

		// Creare oraș
		Oras oras = new Oras();
		oras.setNume("Brasov");
		oras.setDescriere("Oraș montan.");
		oras.setTara(tara);

		// Salvare oraș
		Oras savedOras = orasRepository.save(oras);

		// Verificare
		assertNotNull(savedOras.getTara());
		assertEquals("Romania", savedOras.getTara().getNume());
	}

	@Test
	public void testAddAttractionsToCity() {
		// Creare oraș
		Oras oras = new Oras();
		oras.setNume("Sinaia");
		oras.setDescriere("Oraș turistic.");
		oras = orasRepository.save(oras);

		// Creare atracții
		Atractie atractie1 = new Atractie();
		atractie1.setNume("Castelul Peles");
		atractie1.setDescriere("Castel al regilor României.");
		atractie1.setStatus(FINALIZATA);
		atractie1.setOras(oras);

		Atractie atractie2 = new Atractie();
		atractie2.setNume("Manastirea Sinaia");
		atractie2.setDescriere("Mănăstire ortodoxă.");
		atractie2.setStatus(FINALIZATA);
		atractie2.setOras(oras);

		atractieRepository.save(atractie1);
		atractieRepository.save(atractie2);

		// Recuperare oraș cu atracții
		Oras foundOras = orasRepository.findById(oras.getId()).orElse(null);
		assertNotNull(foundOras);
		assertEquals(2, foundOras.getAtractii().size());
	}

	@Test
	public void testDuplicateCityNames() {
		orasRepository.deleteAll();
		// Creare și salvare orașe cu același nume
		Oras oras1 = new Oras();
		oras1.setNume("Iasi");
		oras1.setDescriere("Oraș universitar.");
		orasRepository.save(oras1);

		Oras oras2 = new Oras();
		oras2.setNume("Iasi");
		oras2.setDescriere("Alt oraș cu același nume.");
		orasRepository.save(oras2);

		// Verificare
		List<Oras> orasList = orasRepository.findAll();
		long count = orasList.stream().filter(o -> o.getNume().equals("Iasi")).count();
		assertEquals(2, count);
	}

	@Test
	public void testUpdateAttraction() {
		// Creare și salvare atracție
		Atractie atractie = new Atractie();
		atractie.setNume("Cetatea Rasnov");
		atractie.setDescriere("Cetate medievală.");
		atractie.setStatus(FINALIZATA);
		Atractie savedAtractie = atractieRepository.save(atractie);

		// Actualizare descriere
		savedAtractie.setDescriere("Cetate fortificată.");
		Atractie updatedAtractie = atractieRepository.save(savedAtractie);

		// Verificare actualizare
		assertEquals("Cetate fortificată.", updatedAtractie.getDescriere());
	}

	@Test
	public void testUpdateCountry() {
		// Creare și salvare țară
		Tara tara = new Tara();
		tara.setNume("Romania");
		Tara savedTara = taraRepository.save(tara);

		// Actualizare nume
		savedTara.setNume("România");
		Tara updatedTara = taraRepository.save(savedTara);

		// Verificare actualizare
		assertEquals("România", updatedTara.getNume());
	}
}