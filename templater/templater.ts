import 'https://deno.land/x/dotenv/load.ts'

// Läd die Pfade der Dateien, die bearbeitet werden sollen
const stdinfiles = Deno.env.get('STDIN')?.split(',')
// Läd die Pfade der Dateien, die erstellt werden sollen
const stdoutfiles = Deno.env.get('STDOUT')?.split(',')

// Wenn die ENV Variablen STDIN und STDOUT nicht gesetzt sind, wird das Programm beendet
if (stdinfiles == undefined || stdoutfiles == undefined) Deno.exit()

// Bearbeitet jede Datei
for (const [index, stdinfile] of stdinfiles.entries()) {
	// Liest/Öffent die zu bearbeitende Datei
	const stdin = Deno.readTextFileSync(stdinfile)

	// Versucht alle Template-Stellen zu ersetzen
	const stdout = stdin.replace(
		/{{\w+}}/g,
		(lücke: string) =>
			Deno.env.get(lücke.replace(/[{}]/g, '').toUpperCase()) || lücke
	)

	// Speichert die neue Datei
	Deno.writeTextFileSync(stdoutfiles[index], stdout)
}
