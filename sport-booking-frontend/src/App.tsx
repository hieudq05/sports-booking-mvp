import { ArrowUpIcon } from "lucide-react"
import { Button } from "./components/ui/button"

function App() {

  return (
    <div className="h-screen w-screen flex items-center justify-center">
      <div className="flex flex-wrap items-center gap-2 md:flex-row">
      <Button variant="outline">Button</Button>
      <Button variant="outline" size="icon" aria-label="Submit">
        <ArrowUpIcon />
      </Button>
    </div>
    </div>
  )
}

export default App
