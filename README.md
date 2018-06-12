# AffinityExample

Пример работы с Ignite Affinity Collocation.
Приложение может добавлять данные в обычный кэш и в кэш, имеющий Affinity Key, а так же получать данные.
Для данных и кэша могут запускаться run, affinityRun, call, affinityCall. Для данных команд измеряется время их выполнения и ведется статистика со средним временем выполения.

Демо режим несколько раз вызывает run, affinityRun, call, affinityCall и выводит статистику времени их выполнения.
Из статистики видно, что affinityRun и affinityCall выполняются быстрее, чем run и call соответственно.

Порядок запуска:

    * app.NodeStartup - запуск узлов хранения (запустить несколько раз),
    * app.ClientNode Startup - запуск клиенского узла, через который происходит работа.
    
Доступные команды:

    demo
    add [cache] [id] [name]
    get [cache] [id]
    run|affRun|call|affCall + [id]
    stat [cache] [command]
    
    cache   - com|per
    id      - if per cache then id format - [perId] [comId]
    command - run|affRun|call|affCall
    
Пример полученной статистики:

    Stat com affCall:   5916879 ns
    Stat com affRun:   12072918 ns
    Stat com call:     13805221 ns
    Stat com run:      28672161 ns
    Stat per affCall:   5797337 ns
    Stat per affRun:    6925095 ns
    Stat per call:     15280844 ns
    Stat per run:      21242467 ns  
   
