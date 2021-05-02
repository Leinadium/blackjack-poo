package model;

import model.cartas.*;

import org.junit.runner.*;
import org.junit.runners.*;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	BlackjackTest.class,
	DealerTest.class,
	FichaTest.class,
	JogadorTest.class,
	MaoTest.class,
	BaralhoTest.class,
	CartasTest.class
})
public class SuiteTest {}
